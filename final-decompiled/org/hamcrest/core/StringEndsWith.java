// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class StringEndsWith extends SubstringMatcher
{
    public StringEndsWith(final String substring) {
        super(substring);
    }
    
    @Override
    protected boolean evalSubstringOf(final String s) {
        return s.endsWith(this.substring);
    }
    
    @Override
    protected String relationship() {
        return "ending with";
    }
    
    @Factory
    public static Matcher<String> endsWith(final String suffix) {
        return new StringEndsWith(suffix);
    }
}
