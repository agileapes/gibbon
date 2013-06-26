package com.agileapes.gibbon.command;

import com.agileapes.gibbon.command.section.Section;
import com.agileapes.gibbon.error.CommandSyntaxException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 18:21)
 */
public interface CommandMatcher {

    Value[] match(Section[] sections, String text) throws CommandSyntaxException;

}
