package com.agileapes.gibbon.command;

import com.agileapes.gibbon.command.impl.DefaultCommandMatcher;
import com.agileapes.gibbon.command.impl.DefaultCommandParser;
import com.agileapes.gibbon.command.section.Section;
import com.agileapes.gibbon.error.CommandSyntaxException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 18:10)
 */
public class CommandMatcherTest {

    private Section[] parse(String text) throws CommandSyntaxException {
        CommandParser parser = new DefaultCommandParser();
        return parser.parse(text);
    }

    @Test
    public void testMatchingOfTerminals() throws Exception {
        final Section[] sections = parse("one two three");
        CommandMatcher matcher = new DefaultCommandMatcher();
        Assert.assertNotNull(matcher.match(sections, "one two three"));
        Assert.assertNull(matcher.match(sections, "one"));
        Assert.assertNull(matcher.match(sections, "one two"));
        Assert.assertNull(matcher.match(sections, "one two three four"));
    }

    @Test
    public void testMatchingOfOptionalTerminals() throws Exception {
        final Section[] sections = parse("one two [three] four");
        CommandMatcher matcher = new DefaultCommandMatcher();
        Assert.assertNotNull(matcher.match(sections, "one two three four"));
        Assert.assertNotNull(matcher.match(sections, "one two four"));
        Assert.assertNull(matcher.match(sections, "one two five four"));
    }

    @Test
    public void testMatchingOfNonTerminals() throws Exception {
        final Section[] sections = parse("copy # to #");
        CommandMatcher matcher = new DefaultCommandMatcher();
        Value[] values = matcher.match(sections, "copy 123 to 456");
        Assert.assertNotNull(values);
        Assert.assertEquals(values.length, 2);
        Assert.assertEquals(values[0].getText(), "123");
        Assert.assertEquals(values[1].getText(), "456");
    }

    @Test
    public void testMatchingOfOptionalNonTerminals() throws Exception {
        final Section[] sections = parse("copy # [and #] to #");
        CommandMatcher matcher = new DefaultCommandMatcher();
        Value[] values = matcher.match(sections, "copy 123 to 456");
        Assert.assertNotNull(values);
        Assert.assertEquals(values.length, 3);
        Assert.assertEquals(values[0].getText(), "123");
        Assert.assertNull(values[1]);
        Assert.assertEquals(values[2].getText(), "456");
        values = matcher.match(sections, "copy 123 and 456 to 789");
        Assert.assertNotNull(values);
        Assert.assertEquals(3, values.length);
        Assert.assertEquals(values[0].getText(), "123");
        Assert.assertEquals(values[1].getText(), "456");
        Assert.assertEquals(values[2].getText(), "789");
    }

    @Test
    public void testMatchingOfMultipleTokensToNonTerminals() throws Exception {
        final Section[] sections = parse("copy # to #");
        CommandMatcher matcher = new DefaultCommandMatcher();
        Value[] values = matcher.match(sections, "copy 'this is gibbon\\'s test' to {123 456 789}");
        Assert.assertNotNull(values);
        Assert.assertEquals(values.length, 2);
        Assert.assertEquals(values[0].getText(), "this is gibbon's test");
        Assert.assertEquals(values[1].getText(), "123 456 789");
    }
}
