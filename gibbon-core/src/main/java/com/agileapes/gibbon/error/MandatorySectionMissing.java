package com.agileapes.gibbon.error;

import com.agileapes.gibbon.command.section.Section;

import java.util.Arrays;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/26, 18:58)
 */
public class MandatorySectionMissing extends CommandSyntaxException {

    public MandatorySectionMissing(Section section) {
        super("Section missing: " + Arrays.toString(section.getTokens().toArray()));
    }

}
