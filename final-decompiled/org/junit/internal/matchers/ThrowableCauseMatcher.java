// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.matchers;

import org.hamcrest.Factory;
import org.hamcrest.SelfDescribing;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ThrowableCauseMatcher<T extends Throwable> extends TypeSafeMatcher<T>
{
    private final Matcher<? extends Throwable> causeMatcher;
    
    public ThrowableCauseMatcher(final Matcher<? extends Throwable> causeMatcher) {
        this.causeMatcher = causeMatcher;
    }
    
    public void describeTo(final Description description) {
        description.appendText("exception with cause ");
        description.appendDescriptionOf(this.causeMatcher);
    }
    
    @Override
    protected boolean matchesSafely(final T item) {
        return this.causeMatcher.matches(item.getCause());
    }
    
    @Override
    protected void describeMismatchSafely(final T item, final Description description) {
        description.appendText("cause ");
        this.causeMatcher.describeMismatch(item.getCause(), description);
    }
    
    @Factory
    public static <T extends Throwable> Matcher<T> hasCause(final Matcher<? extends Throwable> matcher) {
        return new ThrowableCauseMatcher<T>(matcher);
    }
}
