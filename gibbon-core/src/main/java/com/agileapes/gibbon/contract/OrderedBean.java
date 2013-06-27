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
 * This interface will allow its implementers to specify a priority based on which they can be accessed. This is specially
 * useful in specifying {@link BeanProcessor}s and having them ordered to be run in a certain way.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/12/13, 2:57 PM)
 */
public interface OrderedBean {

    /**
     * @return the (dynamic) ordering attributed with the implementing object
     */
    int getOrder();

}
