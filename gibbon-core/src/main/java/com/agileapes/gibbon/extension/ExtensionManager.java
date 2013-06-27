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

package com.agileapes.gibbon.extension;

import com.agileapes.gibbon.command.CommandMatcherAware;
import com.agileapes.gibbon.command.CommandParserAware;
import com.agileapes.gibbon.error.*;
import com.agileapes.gibbon.value.ValueReaderAware;

import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 20:01)
 */
public interface ExtensionManager extends ValueReaderAware, CommandMatcherAware, CommandParserAware {

    void addScheme(ExtensionLoadScheme scheme);

    void addExtension(Object extension) throws UnsupportedExtensionException, CommandSyntaxException, DuplicateExtensionException;

    void load(String name) throws ExtensionAlreadyLoadedException, NoSuchExtensionException;

    void unload(String name) throws ExtensionNotLoadedException, NoSuchExtensionException;

    void execute(String command) throws Exception;

    Set<String> getExtensions();

    boolean isLoaded(String extension) throws NoSuchExtensionException;

}
