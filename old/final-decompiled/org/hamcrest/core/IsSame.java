// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.hamcrest.BaseMatcher;

public class IsSame<T> extends BaseMatcher<T>
{
    private final T object;
    
    public IsSame(final T object) {
        this.object = object;
    }
    
    public boolean matches(final Object arg) {
        return arg == this.object;
    }
    
    public void describeTo(final Description description) {
        description.appendText("sameInstance(").appendValue(this.object).appendText(")");
    }
    
    @Factory
    public static <T> Matcher<T> sameInstance(final T target) {
        return new IsSame<T>(target);
    }
    
    @Factory
    public static <T> Matcher<T> theInstance(final T target) {
        return new IsSame<T>(target);
    }
}
