// 
// Decompiled by Procyon v0.5.36
// 

package org.junit;

import java.util.Arrays;
import org.hamcrest.Matcher;
import org.hamcrest.CoreMatchers;

public class Assume
{
    public static void assumeTrue(final boolean b) {
        assumeThat(b, (Matcher<Boolean>)CoreMatchers.is((T)true));
    }
    
    public static void assumeFalse(final boolean b) {
        assumeTrue(!b);
    }
    
    public static void assumeTrue(final String message, final boolean b) {
        if (!b) {
            throw new AssumptionViolatedException(message);
        }
    }
    
    public static void assumeFalse(final String message, final boolean b) {
        assumeTrue(message, !b);
    }
    
    public static void assumeNotNull(final Object... objects) {
        assumeThat(Arrays.asList(objects), CoreMatchers.everyItem(CoreMatchers.notNullValue()));
    }
    
    public static <T> void assumeThat(final T actual, final Matcher<T> matcher) {
        if (!matcher.matches(actual)) {
            throw new AssumptionViolatedException((T)actual, (Matcher<T>)matcher);
        }
    }
    
    public static <T> void assumeThat(final String message, final T actual, final Matcher<T> matcher) {
        if (!matcher.matches(actual)) {
            throw new AssumptionViolatedException(message, (T)actual, (Matcher<T>)matcher);
        }
    }
    
    public static void assumeNoException(final Throwable e) {
        assumeThat(e, CoreMatchers.nullValue());
    }
    
    public static void assumeNoException(final String message, final Throwable e) {
        assumeThat(message, e, CoreMatchers.nullValue());
    }
}
