package com.agileapes.gibbon.system.impl;

import com.agileapes.gibbon.api.Namespace;
import com.agileapes.gibbon.command.Command;
import com.agileapes.gibbon.command.impl.MethodDelegateCommand;
import com.agileapes.gibbon.contract.Callback;
import com.agileapes.gibbon.contract.Filter;
import com.agileapes.gibbon.system.NamespaceBuilder;
import com.agileapes.gibbon.util.CollectionDSL;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 19:42)
 */
public class AnnotationNamespaceBuilder implements NamespaceBuilder {

    private final Set<Command> commands;

    public AnnotationNamespaceBuilder(Class<?> target) {
        commands = new HashSet<Command>();
        final String namespace = target.isAnnotationPresent(Namespace.class) ? target.getAnnotation(Namespace.class).value() : target.getSimpleName();
        CollectionDSL.with(target.getDeclaredMethods())
                .filter(new Filter<Method>() {
                    @Override
                    public boolean accepts(Method item) {
                        return item.isAnnotationPresent(com.agileapes.gibbon.api.Command.class);
                    }
                })
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
