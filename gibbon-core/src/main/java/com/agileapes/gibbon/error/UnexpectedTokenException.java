package com.agileapes.gibbon.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 18:30)
 */
public class UnexpectedTokenException extends CommandSyntaxException {

    private final String token;
    private final int position;
    private final String command;

    public UnexpectedTokenException(String token, int position, String command) {
        super("Unexpected token '" + token + "' at " + position + " in " + command);
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
