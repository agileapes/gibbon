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

package com.agileapes.gibbon.namespace.impl;

import com.agileapes.gibbon.api.Namespace;
import com.agileapes.gibbon.command.Command;
import com.agileapes.gibbon.command.impl.MethodDelegateCommand;
import com.agileapes.gibbon.contract.Callback;
import com.agileapes.gibbon.namespace.ExtensionLoader;
import com.agileapes.gibbon.util.CollectionDSL;
import com.agileapes.gibbon.util.MethodAnnotationFilter;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 19:42)
 */
public class AnnotationExtensionLoader implements ExtensionLoader {

    private final Set<Command> commands;

    public AnnotationExtensionLoader(Class<?> target) {
        commands = new HashSet<Command>();
        final String namespace = target.isAnnotationPresent(Namespace.class) ? target.getAnnotation(Namespace.class).value() : target.getSimpleName();
        CollectionDSL.with(target.getDeclaredMethods())
                .filter(new MethodAnnotationFilter(com.agileapes.gibbon.api.Command.class))
                .each(new Callback<Method>() {
                    @Override
                    public void perform(Method item) {
                        commands.add(new MethodDelegateCommand(namespace, item.getName(), item.getAnnotation(com.agileapes.gibbon.api.Command.class).value(), item));
                    }
                });
    }

    @Override
    public Set<Command> getCommands() {
        return commands;
    }

}
