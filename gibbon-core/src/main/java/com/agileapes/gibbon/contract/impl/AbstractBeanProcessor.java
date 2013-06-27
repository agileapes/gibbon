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

package com.agileapes.gibbon.contract.impl;

import com.agileapes.gibbon.contract.BeanProcessor;
import com.agileapes.gibbon.contract.OrderedBean;
import com.agileapes.gibbon.error.RegistryException;

/**
 * This is an abstract bean processor that enables you to implement any processor anonymously in a more readable fashion
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/12/13, 3:07 PM)
 */
public abstract class AbstractBeanProcessor implements BeanProcessor, OrderedBean {

    private final int order;

    public AbstractBeanProcessor(int order) {
        this.order = order;
    }

    public AbstractBeanProcessor() {
        this.order = 0;
    }

    @Override
    public Object postProcessBeforeRegistration(Object bean, String beanName) throws RegistryException {
        return bean;
    }

    @Override
    public Object postProcessBeforeDispense(Object bean, String beanName) throws RegistryException {
        return bean;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
