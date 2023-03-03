// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import java.util.List;
import java.util.ArrayList;
import org.hamcrest.Factory;
import org.hamcrest.SelfDescribing;
import java.util.Iterator;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class IsCollectionContaining<T> extends TypeSafeDiagnosingMatcher<Iterable<? super T>>
{
    private final Matcher<? super T> elementMatcher;
    
    public IsCollectionContaining(final Matcher<? super T> elementMatcher) {
        this.elementMatcher = elementMatcher;
    }
    
    @Override
    protected boolean matchesSafely(final Iterable<? super T> collection, final Description mismatchDescription) {
        boolean isPastFirst = false;
        for (final Object item : collection) {
            if (this.elementMatcher.matches(item)) {
                return true;
            }
            if (isPastFirst) {
                mismatchDescription.appendText(", ");
            }
            this.elementMatcher.describeMismatch(item, mismatchDescription);
            isPastFirst = true;
        }
        return false;
    }
    
    public void describeTo(final Description description) {
        description.appendText("a collection containing ").appendDescriptionOf(this.elementMatcher);
    }
    
    @Factory
    public static <T> Matcher<Iterable<? super T>> hasItem(final Matcher<? super T> itemMatcher) {
        return (Matcher<Iterable<? super T>>)new IsCollectionContaining((Matcher<? super Object>)itemMatcher);
    }
    
    @Factory
    public static <T> Matcher<Iterable<? super T>> hasItem(final T item) {
        return (Matcher<Iterable<? super T>>)new IsCollectionContaining((Matcher<? super Object>)IsEqual.equalTo((Object)item));
    }
    
    @Factory
    public static <T> Matcher<Iterable<T>> hasItems(final Matcher<? super T>... itemMatchers) {
        final List<Matcher<? super Iterable<T>>> all = new ArrayList<Matcher<? super Iterable<T>>>(itemMatchers.length);
        for (final Matcher<? super T> elementMatcher : itemMatchers) {
            all.add((Matcher<? super Iterable<T>>)new IsCollectionContaining((Matcher<? super Object>)elementMatcher));
        }
        return AllOf.allOf((Iterable<Matcher<? super Iterable<T>>>)all);
    }
    
    @Factory
    public static <T> Matcher<Iterable<T>> hasItems(final T... items) {
        final List<Matcher<? super Iterable<T>>> all = new ArrayList<Matcher<? super Iterable<T>>>(items.length);
        for (final T element : items) {
            all.add(hasItem(element));
        }
        return AllOf.allOf((Iterable<Matcher<? super Iterable<T>>>)all);
    }
}
