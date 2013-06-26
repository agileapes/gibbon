package com.agileapes.gibbon.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 18:29)
 */
public class CommandSyntaxException extends CommandDefinitionException {

    public CommandSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandSyntaxException(String message) {
        super(message);
    }

}
