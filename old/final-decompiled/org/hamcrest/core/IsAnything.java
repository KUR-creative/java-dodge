// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.hamcrest.BaseMatcher;

public class IsAnything<T> extends BaseMatcher<T>
{
    private final String message;
    
    public IsAnything() {
        this("ANYTHING");
    }
    
    public IsAnything(final String message) {
        this.message = message;
    }
    
    public boolean matches(final Object o) {
        return true;
    }
    
    public void describeTo(final Description description) {
        description.appendText(this.message);
    }
    
    @Factory
    public static Matcher<Object> anything() {
        return new IsAnything<Object>();
    }
    
    @Factory
    public static Matcher<Object> anything(final String description) {
        return new IsAnything<Object>(description);
    }
}
