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

import com.agileapes.gibbon.command.Value;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/27/13, 12:39 PM)
 */
public class MatchedValue extends Value {

    private final int size;
    private final String next;

    public MatchedValue(int size, String next) {
        super(null);
        this.size = size;
        this.next = next;
    }

    public int getSize() {
        return size;
    }

    public String getNext() {
        return next;
    }
}
