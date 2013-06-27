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

package com.agileapes.gibbon.contract;

/**
 * This interface will help the process of replacing an item of a given type with another item (possibly from another
 * type hierarchy).
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/10, 18:56)
 */
public interface Mapper<I, O> {

    /**
     * This method is expected to be able to map a given input object to the desired output object. <strong>Note</strong>
     * that {@code null} values might be acceptable in some contexts, both as the input and as the output
     * @param item    the input item
     * @return the mapped output item
     */
    O map(I item);

}
