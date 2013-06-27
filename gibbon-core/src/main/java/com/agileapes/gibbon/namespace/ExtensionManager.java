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
