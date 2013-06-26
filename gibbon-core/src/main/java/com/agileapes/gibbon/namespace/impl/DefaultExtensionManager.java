package com.agileapes.gibbon.namespace.impl;

import com.agileapes.gibbon.command.*;
import com.agileapes.gibbon.command.impl.DefaultCommandTokenizer;
import com.agileapes.gibbon.command.section.Section;
import com.agileapes.gibbon.contract.Filter;
import com.agileapes.gibbon.contract.Mapper;
import com.agileapes.gibbon.error.*;
import com.agileapes.gibbon.namespace.ExtensionLoadScheme;
import com.agileapes.gibbon.namespace.ExtensionManager;
import com.agileapes.gibbon.util.CollectionDSL;
import com.agileapes.gibbon.value.ValueReader;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 20:12)
 */
public class DefaultExtensionManager implements ExtensionManager {

    private CommandMatcher commandMatcher;
    private CommandParser commandParser;
    private ValueReader valueReader;
    private Set<ExtensionLoadScheme> schemes = new CopyOnWriteArraySet<ExtensionLoadScheme>();
    private Set<String> loaded = new CopyOnWriteArraySet<String>();
    private Set<Command> commands = new CopyOnWriteArraySet<Command>();
    private final CommandTokenizer tokenizer = new DefaultCommandTokenizer();

    private static final class CommandWrapper implements Command {

        private final Command command;
        private final Section[] sections;

        private CommandWrapper(Command command, Section[] sections) {
            this.command = command;
            this.sections = sections;
        }

        @Override
        public String getNamespace() {
            return command.getNamespace();
        }

        @Override
        public String getName() {
            return command.getName();
        }

        @Override
        public String getSyntax() {
            return command.getSyntax();
        }

        @Override
        public void execute(Object... arguments) throws Exception {
            command.execute(arguments);
        }

        @Override
        public Class[] getArguments() {
            return command.getArguments();
        }

        private Section[] getSections() {
            return sections;
        }
    }

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
    public void addExtension(Object extension) throws UnsupportedExtensionException, CommandSyntaxException {
        for (ExtensionLoadScheme scheme : schemes) {
            if (scheme.supports(extension)) {
                final Set<Command> set = scheme.getLoader(extension).getCommands();
                for (Command command : set) {
                    commands.add(new CommandWrapper(command, commandParser.parse(command.getSyntax())));
                }
                return;
            }
        }
        throw new UnsupportedExtensionException(extension);
    }

    @Override
    public void load(String name) throws ExtensionAlreadyLoadedException {
        if (loaded.contains(name)) {
            throw new ExtensionAlreadyLoadedException(name);
        }
        loaded.add(name);
    }

    @Override
    public void unload(String name) throws ExtensionNotLoadedException {
        if (!loaded.contains(name)) {
            throw new ExtensionNotLoadedException(name);
        }
        loaded.remove(name);
    }

    @Override
    public void execute(String text) throws Exception {
        String namespace = null;
        if (text.matches(".*?:")) {
            namespace = text.substring(0, text.indexOf(':')).trim();
            text = text.substring(text.indexOf(':') + 1).trim();
            if (!loaded.contains(namespace)) {
                throw new ExtensionNotLoadedException(namespace);
            }
        }
        final String finalNamespace = namespace;
        final List<String> tokens = tokenizer.tokenize(text);
        final List<Map.Entry<Command, List<Value>>> candidates = CollectionDSL.with(commands)
                .filter(new Filter<Command>() {
                    @Override
                    public boolean accepts(Command item) {
                        return finalNamespace == null || finalNamespace.equals(item.getNamespace());
                    }
                })
                .map(new Mapper<Command, Map.Entry<Command, List<Value>>>() {
                    @Override
                    public Map.Entry<Command, List<Value>> map(Command item) {
                        final CommandWrapper command = (CommandWrapper) item;
                        try {
                            final Value[] values = commandMatcher.match(command.getSections(), tokens);
                            if (values == null) {
                                return null;
                            }
                            return new AbstractMap.SimpleEntry<Command, List<Value>>(command, Arrays.asList(values));
                        } catch (CommandSyntaxException e) {
                            throw new WrappedError(e);
                        }
                    }
                })
                .filter(new Filter<Map.Entry<Command, List<Value>>>() {
                    @Override
                    public boolean accepts(Map.Entry<Command, List<Value>> item) {
                        return item != null;
                    }
                })
                .filter(new Filter<Map.Entry<Command, List<Value>>>() {
                    @Override
                    public boolean accepts(Map.Entry<Command, List<Value>> item) {
                        return item.getKey().getArguments().length == item.getValue().size();
                    }
                }).list();
        if (candidates.size() > 1) {
            throw new AmbiguousCommandException(text);
        } else if (candidates.isEmpty()) {
            throw new BadCommandSyntaxException(text);
        }
        final Map.Entry<Command, List<Value>> entry = candidates.get(0);
        final Command command = entry.getKey();
        final List<Value> values = entry.getValue();
        final Object[] arguments = new Object[command.getArguments().length];
        for (int i = 0; i < arguments.length; i ++) {
            final Value value = values.get(i);
            arguments[i] = valueReader.read(value == null ? null : value.getText(), command.getArguments()[i]);
        }
        command.execute(arguments);
    }

}
