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

package com.agileapes.gibbon.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 18:37)
 */
public class MissingTokenException extends CommandSyntaxException {

    private final String token;
    private final int position;
    private final String command;

    public MissingTokenException(String token, int position, String command) {
        super("Expected token '" + token + "' is missing at " + position + " in " + command);
        this.token = token;
        this.position = position;
        this.command = command;
    }

    public String getToken() {
        return token;
    }

    public int getPosition() {
        return position;
    }

    public String getCommand() {
        return command;
    }

}
