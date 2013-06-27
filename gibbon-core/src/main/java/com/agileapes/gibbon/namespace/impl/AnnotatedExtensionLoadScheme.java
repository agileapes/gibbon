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
