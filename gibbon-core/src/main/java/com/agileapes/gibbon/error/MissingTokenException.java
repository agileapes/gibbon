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
