package com.agileapes.gibbon.system;

import com.agileapes.gibbon.command.Command;
import com.agileapes.gibbon.contract.Callback;
import com.agileapes.gibbon.sample.SampleNamespace;
import com.agileapes.gibbon.system.impl.AnnotationNamespaceBuilder;
import com.agileapes.gibbon.util.CollectionDSL;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 19:16)
 */
public class NamespaceBuilderTest {

    @Test
    public void testNamespace() throws Exception {
        final NamespaceBuilder builder = new AnnotationNamespaceBuilder(SampleNamespace.class);
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
