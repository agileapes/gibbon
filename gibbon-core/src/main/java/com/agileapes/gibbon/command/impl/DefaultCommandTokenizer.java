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

import com.agileapes.gibbon.command.CommandTokenizer;
import com.agileapes.gibbon.error.CommandSyntaxException;
import com.agileapes.gibbon.error.MissingTokenException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 20:33)
 */
public class DefaultCommandTokenizer implements CommandTokenizer {

    private static final Map<Character, Character> pairs = new HashMap<Character, Character>();

    static {
        pairs.put('\'', '\'');
        pairs.put('"', '"');
        pairs.put('`', '`');
        pairs.put('(', ')');
        pairs.put('[', ']');
        pairs.put('{', '}');
    }

    @Override
    public List<String> tokenize(String text) throws CommandSyntaxException {
        final List<String> tokens = new ArrayList<String>();
        int i = 0;
        int marker = 0;
        while (i < text.length()) {
            if (Character.isWhitespace(text.charAt(i))) {
                if (marker < i) {
                    final String token = text.substring(marker, i).trim();
                    if (!token.isEmpty()) {
                        tokens.add(token);
                    }
                }
                i++;
                marker = i;
                continue;
            } else if ("=<>?!~@#$%^&*|\\/".contains(String.valueOf(text.charAt(i)))) {
                if (marker < i) {
                    final String token = text.substring(marker, i).trim();
                    if (!token.isEmpty()) {
                        tokens.add(token);
                    }
                }
                tokens.add(String.valueOf(text.charAt(i)));
                i ++;
                marker = i;
            } else if (pairs.containsKey(text.charAt(i))) {
                if (marker < i) {
                    final String token = text.substring(marker, i).trim();
                    if (!token.isEmpty()) {
                        tokens.add(token);
                    }
                }
                String ending = String.valueOf(pairs.get(text.charAt(i)));
                i ++;
                String token = "";
                while (true) {
                    if (i >= text.length()) {
                        throw new MissingTokenException(ending, i, text);
                    }
                    token += text.charAt(i);
                    i ++;
                    if (token.endsWith(ending) && !token.endsWith("\\" + ending)) {
                        break;
                    }
                }
                token = token.substring(0, token.length() - 1);
                for (Character character : pairs.keySet()) {
                    token = token.replace("\\" + character, character.toString());
                }
                for (Character character : pairs.values()) {
                    token = token.replace("\\" + character, character.toString());
                }
                tokens.add(token);
                marker = i;
            }
            i++;
        }
        if (marker < text.length()) {
            final String token = text.substring(marker, text.length()).trim();
            if (!token.isEmpty()) {
                tokens.add(token);
            }
        }
        return tokens;
    }

}
