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

import com.agileapes.gibbon.error.RegistryException;

/**
 * The registry will act as a central entity "in charge" of containing uniquely named beans of a certain type.
 * Generally, registries should be used for a specific type of objects, and using them as generic contexts for
 * all descendants of {@link Object} will depreciate their value considerably.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/10, 16:42)
 */
public interface Registry<C> {

    /**
     * This method should be called whenever a new bean is to be registered with the registry. Take note that the
     * name should be unique across the context.
     * @param name    the name of the object
     * @param item    the object to be added
     * @throws RegistryException
     */
    void register(String name, C item) throws RegistryException;

    /**
     * This method will give access to the beans defined and registered with the context.
     * @param name    the unique name to the bean
     * @return the bean with the specified name
     * @throws RegistryException
     */
    C get(String name) throws RegistryException;

    /**
     * This method will return an array of all items accepted by the given filters
     * @param filter    the filter
     * @return the array of objects accepted by the filter
     * @throws RegistryException
     */
    C[] find(Filter<C> filter) throws RegistryException;

}
