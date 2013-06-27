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
 * The context interface is an extension to the registry interface which avails the outside world of the benefits
 * of having post processors for registered beans.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/12/13, 2:55 PM)
 */
public interface Context<C> extends Registry<C> {

    /**
     * This method will add a new bean processor to the context. Note that bean processors themselves are not
     * a part of the context.
     * @param beanProcessor    the processor to be added
     */
    void addBeanProcessor(BeanProcessor beanProcessor);

}
