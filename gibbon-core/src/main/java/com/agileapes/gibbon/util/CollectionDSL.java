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

package com.agileapes.gibbon.util;

import com.agileapes.gibbon.contract.Callback;
import com.agileapes.gibbon.contract.Filter;
import com.agileapes.gibbon.contract.Mapper;

import java.util.*;

/**
 * This class will enable you to write easily usable, and fluent workings for any given collection.
 *
 * @see #with(java.util.Collection)
 * @see #with(Object[])
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/10, 18:50)
 */
public abstract class CollectionDSL {

    public static final class Wrapper<I> {

        private final List<I> items;

        private Wrapper(List<I> items) {
            this.items = items;
        }

        public Wrapper<I> filter(Filter<I> filter) {
            final ArrayList<I> list = new ArrayList<I>();
            for (I item : items) {
                if (filter.accepts(item)) {
                    list.add(item);
                }
            }
            return new Wrapper<I>(list);
        }

        public Wrapper<I> each(Callback<I> callback) {
            for (I item : items) {
                callback.perform(item);
            }
            return this;
        }

        public <O> Wrapper<O> map(Mapper<I, O> mapper) {
            final ArrayList<O> list = new ArrayList<O>();
            for (I item : items) {
                list.add(mapper.map(item));
            }
            return new Wrapper<O>(list);
        }

        public List<I> list() {
            return items;
        }

        public I[] array() {
            //noinspection unchecked
            return (I[]) items.toArray();
        }

        public I first() {
            return items.get(0);
        }

        public I last() {
            return items.get(items.size() - 1);
        }

        public <K> Map<K, I> key(Mapper<I, K> mapper) {
            final HashMap<K, I> map = new HashMap<K, I>();
            for (I item : items) {
                map.put(mapper.map(item), item);
            }
            return map;
        }

        public int count() {
            return items.size();
        }

        public boolean isEmpty() {
            return count() == 0;
        }

        public Wrapper<I> sort(Comparator<I> comparator) {
            return new Wrapper<I>(sorted(items, comparator));
        }
    }

    public static <I> Wrapper<I> with(I ... items) {
        return with(Arrays.asList(items));
    }

    public static <I> Wrapper<I> with(Collection<I> collection) {
        return new Wrapper<I>(new ArrayList<I>(collection));
    }

    public static <I extends Comparable<I>> List<I> sorted(Collection<I> input) {
        final ArrayList<I> list = new ArrayList<I>(input);
        Collections.sort(list);
        return list;
    }

    public static <I> List<I> sorted(Collection<I> input, Comparator<I> comparator) {
        final ArrayList<I> list = new ArrayList<I>(input);
        Collections.sort(list, comparator);
        return list;
    }

}
