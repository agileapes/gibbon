package com.agileapes.gibbon.namespace;

import com.agileapes.gibbon.command.CommandMatcherAware;
import com.agileapes.gibbon.command.CommandParserAware;
import com.agileapes.gibbon.error.CommandSyntaxException;
import com.agileapes.gibbon.error.ExtensionAlreadyLoadedException;
import com.agileapes.gibbon.error.ExtensionNotLoadedException;
import com.agileapes.gibbon.error.UnsupportedExtensionException;
import com.agileapes.gibbon.value.ValueReaderAware;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 20:01)
 */
public interface ExtensionManager extends ValueReaderAware, CommandMatcherAware, CommandParserAware {

    void addScheme(ExtensionLoadScheme scheme);

    void addExtension(Object extension) throws UnsupportedExtensionException, CommandSyntaxException;

    void load(String name) throws ExtensionAlreadyLoadedException;

    void unload(String name) throws ExtensionNotLoadedException;

    void execute(String command) throws Exception;

}
