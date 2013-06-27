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

import com.agileapes.gibbon.command.Command;
import com.agileapes.gibbon.contract.Callback;
import com.agileapes.gibbon.extension.impl.AnnotationExtensionLoader;
import com.agileapes.gibbon.sample.SampleNamespace;
import com.agileapes.gibbon.util.CollectionDSL;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 19:16)
 */
public class ExtensionLoaderTest {

    @Test
    public void testNamespace() throws Exception {
        final ExtensionLoader builder = new AnnotationExtensionLoader(SampleNamespace.class);
        final Set<Command> commands = builder.getCommands();
        Assert.assertEquals(commands.size(), 2);
        CollectionDSL.with(commands).each(new Callback<Command>() {
            @Override
            public void perform(Command item) {
                Assert.assertEquals(item.getNamespace(), "sample");
                Assert.assertTrue(Arrays.asList("hi", "count").contains(item.getName()));
            }
        });
    }

}
