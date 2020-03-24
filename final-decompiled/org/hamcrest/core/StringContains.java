// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class StringContains extends SubstringMatcher
{
    public StringContains(final String substring) {
        super(substring);
    }
    
    @Override
    protected boolean evalSubstringOf(final String s) {
        return s.indexOf(this.substring) >= 0;
    }
    
    @Override
    protected String relationship() {
        return "containing";
    }
    
    @Factory
    public static Matcher<String> containsString(final String substring) {
        return new StringContains(substring);
    }
}
