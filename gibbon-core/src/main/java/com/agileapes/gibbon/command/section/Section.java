package com.agileapes.gibbon.command.section;

import java.util.Collections;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/22, 17:56)
 */
public abstract class Section {

    private List<String> tokens;

    protected Section(List<String> tokens) {
        this.tokens = tokens;
    }

    public List<String> getTokens() {
        return Collections.unmodifiableList(tokens);
    }

}
