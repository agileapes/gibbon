package com.agileapes.gibbon.namespace.impl;

import com.agileapes.gibbon.api.Command;
import com.agileapes.gibbon.api.Namespace;
import com.agileapes.gibbon.namespace.ExtensionLoadScheme;
import com.agileapes.gibbon.namespace.ExtensionLoader;
import com.agileapes.gibbon.util.CollectionDSL;
import com.agileapes.gibbon.util.MethodAnnotationFilter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 20:02)
 */
public class AnnotatedExtensionLoadScheme implements ExtensionLoadScheme {

    @Override
    public boolean supports(Object extension) {
        return extension instanceof Class &&
                (((Class) extension).isAnnotationPresent(Namespace.class) ||
                        !CollectionDSL.with(((Class) extension).getDeclaredMethods()).filter(new MethodAnnotationFilter(Command.class)).isEmpty());
    }

    @Override
    public ExtensionLoader getLoader(Object extension) {
        if (supports(extension)) {
            return new AnnotationExtensionLoader((Class<?>) extension);
        }
        return null;
    }

}
