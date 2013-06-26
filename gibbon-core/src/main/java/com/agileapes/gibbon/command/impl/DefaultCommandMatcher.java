package com.agileapes.gibbon.command.impl;

import com.agileapes.gibbon.command.CommandMatcher;
import com.agileapes.gibbon.command.Value;
import com.agileapes.gibbon.command.section.Section;
import com.agileapes.gibbon.command.section.impl.MandatorySection;
import com.agileapes.gibbon.error.CommandSyntaxException;
import com.agileapes.gibbon.error.MissingTokenException;

import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 18:22)
 */
public class DefaultCommandMatcher implements CommandMatcher {

    @Override
    public Value[] match(Section[] sections, List<String> tokens) throws CommandSyntaxException {
        final List<String> values = new ArrayList<String>();
        for (Section section : sections) {
            int matches = matches(section, tokens);
            if (matches > tokens.size()) {
                throw new IllegalStateException();
            }
            if (matches > 0) {
                for (int i = 0; i < section.getTokens().size(); i ++) {
                    if (section.getTokens().get(i).equals("#")) {
                        values.add(tokens.get(i));
                    }
                }
                while (matches -- > 0) {
                    tokens.remove(0);
                }
            } else if (section instanceof MandatorySection) {
                return null;
            } else {
                for (int i = 0; i < section.getTokens().size(); i ++) {
                    if (section.getTokens().get(i).equals("#")) {
                        values.add(null);
                    }
                }
            }
        }
        if (!tokens.isEmpty()) {
            return null;
        }
        final Value[] result = new Value[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i) == null ? null : new Value(values.get(i));
        }
        return result;
    }

    private int matches(Section section, List<String> tokens) {
        if (tokens.size() < section.getTokens().size()) {
            return 0;
        }
        int i;
        for (i = 0; i < section.getTokens().size(); i++) {
            String token = section.getTokens().get(i);
            if (!token.equals("#") && !token.equals(tokens.get(i))) {
                return 0;
            }
        }
        return i;
    }

}
