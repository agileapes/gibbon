package com.agileapes.gibbon.namespace;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 20:02)
 */
public interface ExtensionLoadScheme {

    boolean supports(Object extension);

    ExtensionLoader getLoader(Object extension);

}
