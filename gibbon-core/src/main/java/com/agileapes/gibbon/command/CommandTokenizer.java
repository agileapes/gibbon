package com.agileapes.gibbon.command;

import com.agileapes.gibbon.error.CommandSyntaxException;

import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 20:33)
 */
public interface CommandTokenizer {

    List<String> tokenize(String text) throws CommandSyntaxException;

}
