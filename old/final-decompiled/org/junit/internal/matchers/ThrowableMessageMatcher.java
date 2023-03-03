// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.matchers;

import org.hamcrest.Factory;
import org.hamcrest.SelfDescribing;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ThrowableMessageMatcher<T extends Throwable> extends TypeSafeMatcher<T>
{
    private final Matcher<String> matcher;
    
    public ThrowableMessageMatcher(final Matcher<String> matcher) {
        this.matcher = matcher;
    }
    
    public void describeTo(final Description description) {
        description.appendText("exception with message ");
        description.appendDescriptionOf(this.matcher);
    }
    
    @Override
    protected boolean matchesSafely(final T item) {
        return this.matcher.matches(item.getMessage());
    }
    
    @Override
    protected void describeMismatchSafely(final T item, final Description description) {
        description.appendText("message ");
        this.matcher.describeMismatch(item.getMessage(), description);
    }
    
    @Factory
    public static <T extends Throwable> Matcher<T> hasMessage(final Matcher<String> matcher) {
        return new ThrowableMessageMatcher<T>(matcher);
    }
}
