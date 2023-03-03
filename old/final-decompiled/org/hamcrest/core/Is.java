// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.SelfDescribing;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.BaseMatcher;

public class Is<T> extends BaseMatcher<T>
{
    private final Matcher<T> matcher;
    
    public Is(final Matcher<T> matcher) {
        this.matcher = matcher;
    }
    
    public boolean matches(final Object arg) {
        return this.matcher.matches(arg);
    }
    
    public void describeTo(final Description description) {
        description.appendText("is ").appendDescriptionOf(this.matcher);
    }
    
    @Override
    public void describeMismatch(final Object item, final Description mismatchDescription) {
        this.matcher.describeMismatch(item, mismatchDescription);
    }
    
    @Factory
    public static <T> Matcher<T> is(final Matcher<T> matcher) {
        return new Is<T>(matcher);
    }
    
    @Factory
    public static <T> Matcher<T> is(final T value) {
        return is((Matcher<T>)IsEqual.equalTo((T)value));
    }
    
    @Factory
    @Deprecated
    public static <T> Matcher<T> is(final Class<T> type) {
        final Matcher<T> typeMatcher = IsInstanceOf.instanceOf(type);
        return is(typeMatcher);
    }
    
    @Factory
    public static <T> Matcher<T> isA(final Class<T> type) {
        final Matcher<T> typeMatcher = IsInstanceOf.instanceOf(type);
        return is(typeMatcher);
    }
}
