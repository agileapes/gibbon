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

    private static final Map<Character, Character> pairs = new HashMap<Character, Character>();

    static {
        pairs.put('\'', '\'');
        pairs.put('"', '"');
        pairs.put('`', '`');
        pairs.put('(', ')');
        pairs.put('[', ']');
        pairs.put('{', '}');
    }

    private List<String> tokenize(String text) throws MissingTokenException {
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
            } else if ("+-=<>?!~@#$%^&*|\\/".contains(String.valueOf(text.charAt(i)))) {
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

    @Override
    public Value[] match(Section[] sections, String text) throws CommandSyntaxException {
        final List<String> tokens = tokenize(text);
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
