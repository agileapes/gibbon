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
