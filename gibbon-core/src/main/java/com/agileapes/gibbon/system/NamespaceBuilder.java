package com.agileapes.gibbon.system;

import com.agileapes.gibbon.command.Command;

import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 19:42)
 */
public interface NamespaceBuilder {

    Set<Command> getCommands();

}
