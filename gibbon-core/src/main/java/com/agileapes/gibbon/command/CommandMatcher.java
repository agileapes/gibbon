package com.agileapes.gibbon.command;

import com.agileapes.gibbon.command.section.Section;
import com.agileapes.gibbon.error.CommandSyntaxException;

import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 18:21)
 */
public interface CommandMatcher {

    Value[] match(Section[] sections, List<String> tokens) throws CommandSyntaxException;

}
