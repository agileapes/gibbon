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
import com.agileapes.gibbon.contract.Context;
import com.agileapes.gibbon.contract.Filter;
import com.agileapes.gibbon.contract.OrderedBean;
import com.agileapes.gibbon.error.DuplicateItemException;
import com.agileapes.gibbon.error.InvalidItemNameException;
import com.agileapes.gibbon.error.NoSuchItemException;
import com.agileapes.gibbon.error.RegistryException;
import com.agileapes.gibbon.util.CollectionDSL;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The abstract context implements all the basic functionalities expected from any context, leaving the map to the beans
 * to be defined by the extending class
 *
 * @see #getMap()
 * @see #namesAreTypeSpecific
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/10, 16:44)
 */
public abstract class AbstractContext<C> implements Context<C> {

    /**
     * @return the map which will contain the registry of the beans
     */
    protected abstract Map<String, C> getMap();

    protected abstract Class<C> getType();

    /**
     * This parameter, if set to {@link true} will enforce a policy for registration of beans that will require all
     * bean names to be equal to the canonical names of their defining classes
     */
    protected boolean namesAreTypeSpecific = false;
    private List<BeanProcessor> beanProcessors = new CopyOnWriteArrayList<BeanProcessor>();

    @Override
    public void register(String name, C item) throws RegistryException {
        if (getMap().containsKey(name)) {
            throw new DuplicateItemException(name);
        }
        if (namesAreTypeSpecific && !item.getClass().getCanonicalName().equals(name)) {
            throw new InvalidItemNameException(name, item.getClass().getCanonicalName());
        }
        C changed = postProcessBeforeRegister(name, item);
        if (changed == null) {
            changed = item;
        }
        getMap().put(name, changed);
    }

    @Override
    public C get(String name) throws RegistryException {
        if (!getMap().containsKey(name)) {
            throw new NoSuchItemException(name);
        }
        return postProcessBeforeDispense(name, getMap().get(name));
    }

    @Override
    public C[] find(Filter<C> filter) throws RegistryException {
        if (getMap().isEmpty()) {
            throw new NoSuchItemException(null);
        }
        final Set<C> set = new HashSet<C>();
        for (Map.Entry<String, C> entry : getMap().entrySet()) {
            if (filter.accepts(entry.getValue())) {
                set.add(postProcessBeforeDispense(entry.getKey(), entry.getValue()));
            }
        }
        //noinspection unchecked
        return set.toArray((C[]) Array.newInstance(getType(), set.size()));
    }

    @SuppressWarnings("unchecked")
    private C postProcessBeforeRegister(String name, C item) throws RegistryException {
        C bean = item;
        for (BeanProcessor processor : beanProcessors) {
            Object changed = processor.postProcessBeforeRegistration(bean, name);
            if (changed == null) {
                changed = bean;
            }
            bean = (C) changed;
        }
        return bean;
    }

    @SuppressWarnings("unchecked")
    private C postProcessBeforeDispense(String name, C item) throws RegistryException {
        C bean = item;
        for (BeanProcessor processor : beanProcessors) {
            Object changed = processor.postProcessBeforeDispense(bean, name);
            if (changed == null) {
                changed = bean;
            }
            bean = (C) changed;
        }
        return bean;
    }

    @Override
    public void addBeanProcessor(BeanProcessor beanProcessor) {
        beanProcessors.add(beanProcessor);
        beanProcessors = CollectionDSL.sorted(beanProcessors, new Comparator<BeanProcessor>() {
            @Override
            public int compare(BeanProcessor o1, BeanProcessor o2) {
                final Integer first = o1 instanceof OrderedBean ? ((OrderedBean) o1).getOrder() : 0;
                final Integer second = o2 instanceof OrderedBean ? ((OrderedBean) o2).getOrder() : 0;
                return first.compareTo(second);
            }
        });
    }

    public List<BeanProcessor> getBeanProcessors() {
        return beanProcessors;
    }

    public Collection<C> getBeans() {
        return getMap().values();
    }

    public List<C> getOrderedBeans() {
        return CollectionDSL.sorted(getBeans(), new Comparator<C>() {
            @Override
            public int compare(C o1, C o2) {
                final Integer first = o1 instanceof OrderedBean ? ((OrderedBean) o1).getOrder() : 0;
                final Integer second = o2 instanceof OrderedBean ? ((OrderedBean) o2).getOrder() : 0;
                return first.compareTo(second);
            }
        });
    }

}
