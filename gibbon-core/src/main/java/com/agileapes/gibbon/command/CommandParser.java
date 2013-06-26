package com.agileapes.gibbon.command;

import com.agileapes.gibbon.command.section.Section;
import com.agileapes.gibbon.error.CommandSyntaxException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/22, 17:53)
 */
public interface CommandParser {

    Section[] parse(String text) throws CommandSyntaxException;

}
