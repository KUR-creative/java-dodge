// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;

public class IsInstanceOf extends DiagnosingMatcher<Object>
{
    private final Class<?> expectedClass;
    private final Class<?> matchableClass;
    
    public IsInstanceOf(final Class<?> expectedClass) {
        this.expectedClass = expectedClass;
        this.matchableClass = matchableClass(expectedClass);
    }
    
    private static Class<?> matchableClass(final Class<?> expectedClass) {
        if (Boolean.TYPE.equals(expectedClass)) {
            return Boolean.class;
        }
        if (Byte.TYPE.equals(expectedClass)) {
            return Byte.class;
        }
        if (Character.TYPE.equals(expectedClass)) {
            return Character.class;
        }
        if (Double.TYPE.equals(expectedClass)) {
            return Double.class;
        }
        if (Float.TYPE.equals(expectedClass)) {
            return Float.class;
        }
        if (Integer.TYPE.equals(expectedClass)) {
            return Integer.class;
        }
        if (Long.TYPE.equals(expectedClass)) {
            return Long.class;
        }
        if (Short.TYPE.equals(expectedClass)) {
            return Short.class;
        }
        return expectedClass;
    }
    
    @Override
    protected boolean matches(final Object item, final Description mismatch) {
        if (null == item) {
            mismatch.appendText("null");
            return false;
        }
        if (!this.matchableClass.isInstance(item)) {
            mismatch.appendValue(item).appendText(" is a " + item.getClass().getName());
            return false;
        }
        return true;
    }
    
    public void describeTo(final Description description) {
        description.appendText("an instance of ").appendText(this.expectedClass.getName());
    }
    
    @Factory
    public static <T> Matcher<T> instanceOf(final Class<?> type) {
        return (Matcher<T>)new IsInstanceOf(type);
    }
    
    @Factory
    public static <T> Matcher<T> any(final Class<T> type) {
        return (Matcher<T>)new IsInstanceOf(type);
    }
}
