// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.SelfDescribing;
import java.util.Iterator;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class Every<T> extends TypeSafeDiagnosingMatcher<Iterable<T>>
{
    private final Matcher<? super T> matcher;
    
    public Every(final Matcher<? super T> matcher) {
        this.matcher = matcher;
    }
    
    public boolean matchesSafely(final Iterable<T> collection, final Description mismatchDescription) {
        for (final T t : collection) {
            if (!this.matcher.matches(t)) {
                mismatchDescription.appendText("an item ");
                this.matcher.describeMismatch(t, mismatchDescription);
                return false;
            }
        }
        return true;
    }
    
    public void describeTo(final Description description) {
        description.appendText("every item is ").appendDescriptionOf(this.matcher);
    }
    
    @Factory
    public static <U> Matcher<Iterable<U>> everyItem(final Matcher<U> itemMatcher) {
        return (Matcher<Iterable<U>>)new Every((Matcher<? super Object>)itemMatcher);
    }
}
