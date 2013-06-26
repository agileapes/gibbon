package com.agileapes.gibbon.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 18:28)
 */
public abstract class CommandDefinitionException extends Exception {

    public CommandDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandDefinitionException(String message) {
        super(message);
    }

}
