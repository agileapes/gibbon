/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.gibbon.extension.impl;

import com.agileapes.gibbon.command.*;
import com.agileapes.gibbon.command.impl.CommandWrapper;
import com.agileapes.gibbon.command.impl.DefaultCommandTokenizer;
import com.agileapes.gibbon.command.impl.MatchedValue;
import com.agileapes.gibbon.contract.Filter;
import com.agileapes.gibbon.contract.Mapper;
import com.agileapes.gibbon.error.*;
import com.agileapes.gibbon.extension.ExtensionLoadScheme;
import com.agileapes.gibbon.extension.ExtensionLoader;
import com.agileapes.gibbon.extension.ExtensionManager;
import com.agileapes.gibbon.util.CollectionDSL;
import com.agileapes.gibbon.value.ValueReader;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 20:12)
 */
public class DefaultExtensionManager implements ExtensionManager {

    private final Set<String> extensions = new CopyOnWriteArraySet<String>();
    private CommandMatcher commandMatcher;
    private CommandParser commandParser;
    private ValueReader valueReader;
    private Set<ExtensionLoadScheme> schemes = new CopyOnWriteArraySet<ExtensionLoadScheme>();
    private Set<String> loaded = new CopyOnWriteArraySet<String>();
    private Set<CommandWrapper> commands = new CopyOnWriteArraySet<CommandWrapper>();
    private final CommandTokenizer tokenizer = new DefaultCommandTokenizer();

    @Override
    public void setValueReader(ValueReader valueReader) {
        this.valueReader = valueReader;
    }

    @Override
    public void setCommandParser(CommandParser commandParser) {
        this.commandParser = commandParser;
    }

    @Override
    public void setCommandMatcher(CommandMatcher commandMatcher) {
        this.commandMatcher = commandMatcher;
    }

    @Override
    public void addScheme(ExtensionLoadScheme scheme) {
        schemes.add(scheme);
    }

    @Override
    public void addExtension(Object extension) throws UnsupportedExtensionException, CommandSyntaxException, DuplicateExtensionException {
        for (ExtensionLoadScheme scheme : schemes) {
            if (scheme.supports(extension)) {
                final ExtensionLoader loader = scheme.getLoader(extension);
                if (extensions.contains(loader.getName())) {
                    throw new DuplicateExtensionException(loader.getName());
                }
                extensions.add(loader.getName());
                final Set<Command> set = loader.getCommands();
                for (Command command : set) {
                    commands.add(new CommandWrapper(command, commandParser.parse(command.getSyntax())));
                }
                return;
            }
        }
        throw new UnsupportedExtensionException(extension);
    }

    @Override
    public void load(String name) throws ExtensionAlreadyLoadedException, NoSuchExtensionException {
        if (!extensions.contains(name)) {
            throw new NoSuchExtensionException(name);
        }
        if (loaded.contains(name)) {
            throw new ExtensionAlreadyLoadedException(name);
        }
        loaded.add(name);
    }

    @Override
    public void unload(String name) throws ExtensionNotLoadedException, NoSuchExtensionException {
        if (!extensions.contains(name)) {
            throw new NoSuchExtensionException(name);
        }
        if (!loaded.contains(name)) {
            throw new ExtensionNotLoadedException(name);
        }
        loaded.remove(name);
    }

    @Override
    public void execute(String text) throws Exception {
        String namespace = null;
        if (text.matches(".*?:.*")) {
            namespace = text.substring(0, text.indexOf(':')).trim();
            text = text.substring(text.indexOf(':') + 1).trim();
            if (!loaded.contains(namespace)) {
                throw new ExtensionNotLoadedException(namespace);
            }
        }
        final String finalNamespace = namespace;
        final List<String> tokens = tokenizer.tokenize(text);
        final List<Map.Entry<CommandWrapper, List<Value>>> candidates = CollectionDSL.with(commands)
                .filter(new Filter<CommandWrapper>() {
                    @Override
                    public boolean accepts(CommandWrapper item) {
                        return finalNamespace == null || finalNamespace.equals(item.getNamespace());
                    }
                })
                .map(new Mapper<CommandWrapper, Map.Entry<CommandWrapper, List<Value>>>() {
                    @Override
                    public Map.Entry<CommandWrapper, List<Value>> map(CommandWrapper command) {
                        try {
                            final Value[] values = commandMatcher.match(command.getSections(), tokens);
                            if (values == null || (values.length == 1 && values[0] instanceof MatchedValue)) {
                                return null;
                            }
                            return new AbstractMap.SimpleEntry<CommandWrapper, List<Value>>(command, Arrays.asList(values));
                        } catch (CommandSyntaxException e) {
                            throw new WrappedError(e);
                        }
                    }
                })
                .filter(new Filter<Map.Entry<CommandWrapper, List<Value>>>() {
                    @Override
                    public boolean accepts(Map.Entry<CommandWrapper, List<Value>> item) {
                        return item != null;
                    }
                })
                .filter(new Filter<Map.Entry<CommandWrapper, List<Value>>>() {
                    @Override
                    public boolean accepts(Map.Entry<CommandWrapper, List<Value>> item) {
                        return item.getKey().getArguments().length == item.getValue().size();
                    }
                }).list();
        if (candidates.size() > 1) {
            throw new AmbiguousCommandException(text);
        } else if (candidates.isEmpty()) {
            throw new BadCommandSyntaxException(text);
        }
        final Map.Entry<CommandWrapper, List<Value>> entry = candidates.get(0);
        final CommandWrapper command = entry.getKey();
        final List<Value> values = entry.getValue();
        final Object[] arguments = new Object[command.getArguments().length];
        for (int i = 0; i < arguments.length; i ++) {
            final Value value = values.get(i);
            arguments[i] = value == null ? null : valueReader.read(value.getText(), command.getArguments()[i]);
        }
        command.execute(arguments);
    }

    @Override
    public Set<String> getExtensions() {
        return extensions;
    }

    @Override
    public boolean isLoaded(String extension) throws NoSuchExtensionException {
        if (!extensions.contains(extension)) {
            throw new NoSuchExtensionException(extension);
        }
        return loaded.contains(extension);
    }

    public Set<CommandWrapper> getCommands() {
        return Collections.unmodifiableSet(commands);
    }

    public List<String> getNext(String text, boolean partial) throws ExtensionNotLoadedException, CommandSyntaxException {
        String namespace = null;
        if (text.matches(".*?:.*")) {
            namespace = text.substring(0, text.indexOf(':')).trim();
            text = text.substring(text.indexOf(':') + 1).trim();
            if (!loaded.contains(namespace)) {
                throw new ExtensionNotLoadedException(namespace);
            }
        }
        final String finalNamespace = namespace;
        final List<String> tokens = tokenizer.tokenize(text);
        String current = null;
        if (partial) {
            current = tokens.get(tokens.size() - 1);
            tokens.remove(tokens.size() - 1);
        }
        CollectionDSL.Wrapper<String> wrapper = CollectionDSL.with(commands)
                //filtering by namespace
                .filter(new Filter<CommandWrapper>() {
                    @Override
                    public boolean accepts(CommandWrapper item) {
                        return finalNamespace == null || finalNamespace.equals(item.getNamespace());
                    }
                })
                        //mapping to matched values
                .map(new Mapper<CommandWrapper, MatchedValue>() {
                    @Override
                    public MatchedValue map(CommandWrapper command) {
                        try {
                            final Value[] values = commandMatcher.match(command.getSections(), tokens);
                            if (values == null || !(values.length == 1 && values[0] instanceof MatchedValue)) {
                                return null;
                            }
                            return (MatchedValue) values[0];
                        } catch (CommandSyntaxException e) {
                            throw new WrappedError(e);
                        }
                    }
                })
                        //filtering those that are complete or not a match at all
                .filter(new Filter<MatchedValue>() {
                    @Override
                    public boolean accepts(MatchedValue item) {
                        return item != null && item.getSize() > 0;
                    }
                })
                        //sorting by matching length
                .sort(new Comparator<MatchedValue>() {
                    @Override
                    public int compare(MatchedValue o1, MatchedValue o2) {
                        return new Integer(o2.getSize()).compareTo(o1.getSize());
                    }
                })
                        //mapping to next token
                .map(new Mapper<MatchedValue, String>() {
                    @Override
                    public String map(MatchedValue item) {
                        return item.getNext();
                    }
                });
        if (partial) {
            final String finalCurrent = current;
            wrapper = wrapper.filter(new Filter<String>() {
                @Override
                public boolean accepts(String item) {
                    return item.startsWith(finalCurrent);
                }
            });
        }
        return wrapper.list();
    }



}
