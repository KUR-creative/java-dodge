// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class StringStartsWith extends SubstringMatcher
{
    public StringStartsWith(final String substring) {
        super(substring);
    }
    
    @Override
    protected boolean evalSubstringOf(final String s) {
        return s.startsWith(this.substring);
    }
    
    @Override
    protected String relationship() {
        return "starting with";
    }
    
    @Factory
    public static Matcher<String> startsWith(final String prefix) {
        return new StringStartsWith(prefix);
    }
}
