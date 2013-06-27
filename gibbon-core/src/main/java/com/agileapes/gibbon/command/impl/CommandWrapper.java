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

package com.agileapes.gibbon.command.impl;

import com.agileapes.gibbon.command.Command;
import com.agileapes.gibbon.command.section.Section;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 12:51 PM)
 */
public class CommandWrapper implements Command {

    private final Command command;
    private final Section[] sections;

    public CommandWrapper(Command command, Section[] sections) {
        this.command = command;
        this.sections = sections;
    }

    @Override
    public String getNamespace() {
        return command.getNamespace();
    }

    @Override
    public String getName() {
        return command.getName();
    }

    @Override
    public String getSyntax() {
        return command.getSyntax();
    }

    @Override
    public void execute(Object... arguments) throws Exception {
        command.execute(arguments);
    }

    @Override
    public Class[] getArguments() {
        return command.getArguments();
    }

    public Section[] getSections() {
        return sections;
    }
}

