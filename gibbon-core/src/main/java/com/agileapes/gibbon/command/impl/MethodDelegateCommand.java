package com.agileapes.gibbon.command.impl;

import com.agileapes.gibbon.command.Command;

import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 19:46)
 */
public class MethodDelegateCommand implements Command {

    private final String namespace;
    private final String name;
    private final String syntax;
    private final Method method;

    public MethodDelegateCommand(String namespace, String name, String syntax, Method method) {
        this.namespace = namespace;
        this.name = name;
        this.syntax = syntax;
        this.method = method;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSyntax() {
        return syntax;
    }

    @Override
    public Class[] getArguments() {
        return method.getParameterTypes();
    }

    @Override
    public void execute(Object... arguments) throws Exception {
        final Class<?> declaringClass = method.getDeclaringClass();
        final Object instance = declaringClass.newInstance();
        method.invoke(instance, arguments);
    }

}