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

package com.agileapes.gibbon.command;

import com.agileapes.gibbon.command.impl.DefaultCommandParser;
import com.agileapes.gibbon.command.impl.OptionalSection;
import com.agileapes.gibbon.command.section.Section;
import com.agileapes.gibbon.command.section.impl.MandatorySection;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/22, 17:52)
 */
public class CommandParserTest {

    @Test
    public void testParseSimple() throws Exception {
        final CommandParser parser = new DefaultCommandParser();
        Section[] sections = parser.parse("test");
        Assert.assertEquals(sections.length, 1);
        final Section section = sections[0];
        Assert.assertTrue(section instanceof MandatorySection);
        Assert.assertFalse(section.getTokens().isEmpty());
        Assert.assertEquals(section.getTokens().size(), 1);
        Assert.assertEquals(section.getTokens().get(0), "test");
    }

    @Test
    public void testParseMultiple() throws Exception {
        final CommandParser parser = new DefaultCommandParser();
        Section[] sections = parser.parse("this is a test");
        Assert.assertEquals(sections.length, 1);
        final Section section = sections[0];
        Assert.assertTrue(section instanceof MandatorySection);
        Assert.assertFalse(section.getTokens().isEmpty());
        Assert.assertEquals(section.getTokens().size(), 4);
        Assert.assertEquals(section.getTokens().get(0), "this");
        Assert.assertEquals(section.getTokens().get(1), "is");
        Assert.assertEquals(section.getTokens().get(2), "a");
        Assert.assertEquals(section.getTokens().get(3), "test");
    }

    @Test
    public void testParseOptional() throws Exception {
        final CommandParser parser = new DefaultCommandParser();
        Section[] sections = parser.parse("[#]");
        Assert.assertEquals(sections.length, 1);
        final Section section = sections[0];
        Assert.assertTrue(section instanceof OptionalSection);
        Assert.assertFalse(section.getTokens().isEmpty());
        Assert.assertEquals(section.getTokens().size(), 1);
        Assert.assertEquals(section.getTokens().get(0), "#");
    }

    @Test
    public void testParseOptionalAndMandatory() throws Exception {
        final CommandParser parser = new DefaultCommandParser();
        Section[] sections = parser.parse("[#] is good");
        Assert.assertEquals(sections.length, 2);
        final Section first = sections[0];
        final Section second = sections[1];
        Assert.assertTrue(first instanceof OptionalSection);
        Assert.assertEquals(first.getTokens().size(), 1);
        Assert.assertEquals(first.getTokens().get(0), "#");
        Assert.assertTrue(second instanceof MandatorySection);
        Assert.assertEquals(second.getTokens().size(), 2);
        Assert.assertEquals(second.getTokens().get(0), "is");
        Assert.assertEquals(second.getTokens().get(1), "good");
    }

    @Test
    public void testParseComplex() throws Exception {
        final CommandParser parser = new DefaultCommandParser();
        Section[] sections = parser.parse("copy # to # [and mirror to #]");
        Assert.assertEquals(sections.length, 2);
        final Section first = sections[0];
        final Section second = sections[1];
        Assert.assertTrue(first instanceof MandatorySection);
        Assert.assertEquals(first.getTokens().size(), 4);
        Assert.assertEquals(first.getTokens().get(0), "copy");
        Assert.assertEquals(first.getTokens().get(1), "#");
        Assert.assertEquals(first.getTokens().get(2), "to");
        Assert.assertEquals(first.getTokens().get(3), "#");
        Assert.assertTrue(second instanceof OptionalSection);
        Assert.assertEquals(second.getTokens().size(), 4);
        Assert.assertEquals(second.getTokens().get(0), "and");
        Assert.assertEquals(second.getTokens().get(1), "mirror");
        Assert.assertEquals(second.getTokens().get(2), "to");
        Assert.assertEquals(second.getTokens().get(3), "#");
    }

}
