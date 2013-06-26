package com.agileapes.gibbon;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/22, 17:46)
 */
public interface Command {

    String getNamespace();

    String getName();

    String getSyntax();

}
