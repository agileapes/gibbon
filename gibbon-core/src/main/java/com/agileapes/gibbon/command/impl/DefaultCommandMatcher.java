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

import com.agileapes.gibbon.command.CommandMatcher;
import com.agileapes.gibbon.command.Value;
import com.agileapes.gibbon.command.section.Section;
import com.agileapes.gibbon.command.section.impl.MandatorySection;
import com.agileapes.gibbon.error.CommandSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 18:22)
 */
public class DefaultCommandMatcher implements CommandMatcher {

    @Override
    public Value[] match(Section[] sections, List<String> tokens) throws CommandSyntaxException {
        final List<String> values = new ArrayList<String>();
        int matched = 0;
        final Map<Integer, Integer> matchedSections = new HashMap<Integer, Integer>();
        for (int index = 0; index < sections.length; index++) {
            Section section = sections[index];
            int matches = matches(section, tokens);
            matchedSections.put(index, matches);
            if (matches > tokens.size()) {
                throw new IllegalStateException();
            }
            if (matches == section.getTokens().size()) {
                for (int i = 0; i < section.getTokens().size(); i++) {
                    if (section.getTokens().get(i).equals("#")) {
                        values.add(tokens.get(i));
                    }
                }
                while (matches-- > 0) {
                    matched++;
                    tokens.remove(0);
                }
            } else if (section instanceof MandatorySection) {
                while (matches-- > 0) {
                    matched++;
                    tokens.remove(0);
                }
                if (tokens.isEmpty()) {
                    return new Value[]{new MatchedValue(matched, section.getTokens().get(matchedSections.get(index)))};
                } else {
                    while (index > 0 && sections[-- index] instanceof OptionalSection) {
                        if (matchedSections.get(index) == 0) {
                            continue;
                        }
                        return new Value[]{new MatchedValue(matched, sections[index].getTokens().get(matchedSections.get(index)))};
                    }
                }
                return null;
            } else {
                for (int i = 0; i < section.getTokens().size(); i++) {
                    if (section.getTokens().get(i).equals("#")) {
                        values.add(null);
                    }
                }
            }
        }
        if (!tokens.isEmpty()) {
            int index = sections.length;
            while (index > 0 && sections[-- index] instanceof OptionalSection) {
                if (matchedSections.get(index) == 0) {
                    continue;
                }
                return new Value[]{new MatchedValue(matched, sections[index].getTokens().get(matchedSections.get(index)))};
            }
            return null;
        }
        final Value[] result = new Value[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i) == null ? null : new Value(values.get(i));
        }
        return result;
    }

    private int matches(Section section, List<String> tokens) {
        int i;
        for (i = 0; i < Math.min(section.getTokens().size(), tokens.size()); i++) {
            String token = section.getTokens().get(i);
            if (!token.equals("#") && !token.equals(tokens.get(i))) {
                break;
            }
        }
        return i;
    }

}
