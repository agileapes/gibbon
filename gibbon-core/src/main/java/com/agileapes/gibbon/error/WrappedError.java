package com.agileapes.gibbon.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 20:38)
 */
public class WrappedError extends Error {
    public WrappedError(Throwable e) {
        super(e);
    }
}
