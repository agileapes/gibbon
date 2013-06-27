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

package com.agileapes.gibbon.sample;

import com.agileapes.gibbon.api.Command;
import com.agileapes.gibbon.api.Namespace;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 19:16)
 */
@Namespace("sample")
public class SampleNamespace {

    @Command("say hi [to #]")
    public void hi(String name) {
        if (name == null) {
            System.err.println("Hello world!");
        } else {
            System.err.println("Hello, " + name + "!");
        }
    }

    @Command("count [from #] to #")
    public void count(Integer from, Integer to) {
        if (from == null) {
            from = to > 0 ? 0 : to;
        }
        while (to >= from) {
            System.err.println(to --);
        }
    }

}
