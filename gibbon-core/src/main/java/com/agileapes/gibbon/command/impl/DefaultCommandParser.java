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

package com.agileapes.gibbon.command.impl;

import com.agileapes.gibbon.command.CommandParser;
import com.agileapes.gibbon.command.section.Section;
import com.agileapes.gibbon.command.section.impl.MandatorySection;
import com.agileapes.gibbon.error.CommandSyntaxException;
import com.agileapes.gibbon.error.MissingTokenException;
import com.agileapes.gibbon.error.UnexpectedTokenException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/22, 17:54)
 */
public class DefaultCommandParser implements CommandParser {

    @Override
    public Section[] parse(String text) throws CommandSyntaxException {
        final List<Section> sections = new ArrayList<Section>();
        int i = 0;
        int marker = 0;
        boolean optional = false;
        while (i < text.length()) {
            if (text.charAt(i) == '[') {
                if (optional) {
                    throw new UnexpectedTokenException("[", i, text);
                }
                optional = true;
                final String substring = text.substring(marker, i).trim();
                if (!substring.isEmpty()) {
                    sections.add(new MandatorySection(Arrays.asList(substring.split("\\s+"))));
                }
                i ++;
                marker = i;
                continue;
            } else if (text.charAt(i) == ']') {
                if (!optional) {
                    throw new UnexpectedTokenException("]", i, text);
                }
                optional = false;
                final String substring = text.substring(marker, i).trim();
                if (!substring.isEmpty()) {
                    sections.add(new OptionalSection(Arrays.asList(substring.split("\\s+"))));
                }
                i ++;
                marker = i;
                continue;
            }
            i ++;
        }
        if (optional) {
            throw new MissingTokenException("]", text.length(), text);
        }
        if (marker < text.length()) {
            final String substring = text.substring(marker, text.length()).trim();
            if (!substring.isEmpty()) {
                sections.add(new MandatorySection(Arrays.asList(substring.split("\\s+"))));
            }
        }
        return sections.toArray(new Section[sections.size()]);
    }

}
