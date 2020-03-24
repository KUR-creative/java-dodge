// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.SelfDescribing;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.BaseMatcher;

public class IsNot<T> extends BaseMatcher<T>
{
    private final Matcher<T> matcher;
    
    public IsNot(final Matcher<T> matcher) {
        this.matcher = matcher;
    }
    
    public boolean matches(final Object arg) {
        return !this.matcher.matches(arg);
    }
    
    public void describeTo(final Description description) {
        description.appendText("not ").appendDescriptionOf(this.matcher);
    }
    
    @Factory
    public static <T> Matcher<T> not(final Matcher<T> matcher) {
        return new IsNot<T>(matcher);
    }
    
    @Factory
    public static <T> Matcher<T> not(final T value) {
        return not((Matcher<T>)IsEqual.equalTo((T)value));
    }
}
