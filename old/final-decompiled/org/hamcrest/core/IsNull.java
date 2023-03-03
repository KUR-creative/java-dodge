// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.hamcrest.BaseMatcher;

public class IsNull<T> extends BaseMatcher<T>
{
    public boolean matches(final Object o) {
        return o == null;
    }
    
    public void describeTo(final Description description) {
        description.appendText("null");
    }
    
    @Factory
    public static Matcher<Object> nullValue() {
        return new IsNull<Object>();
    }
    
    @Factory
    public static Matcher<Object> notNullValue() {
        return IsNot.not(nullValue());
    }
    
    @Factory
    public static <T> Matcher<T> nullValue(final Class<T> type) {
        return new IsNull<T>();
    }
    
    @Factory
    public static <T> Matcher<T> notNullValue(final Class<T> type) {
        return IsNot.not((Matcher<T>)nullValue((Class<T>)type));
    }
}
