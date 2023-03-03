// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.hamcrest.Factory;
import java.util.Iterator;
import org.hamcrest.SelfDescribing;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.DiagnosingMatcher;

public class AllOf<T> extends DiagnosingMatcher<T>
{
    private final Iterable<Matcher<? super T>> matchers;
    
    public AllOf(final Iterable<Matcher<? super T>> matchers) {
        this.matchers = matchers;
    }
    
    public boolean matches(final Object o, final Description mismatch) {
        for (final Matcher<? super T> matcher : this.matchers) {
            if (!matcher.matches(o)) {
                mismatch.appendDescriptionOf(matcher).appendText(" ");
                matcher.describeMismatch(o, mismatch);
                return false;
            }
        }
        return true;
    }
    
    public void describeTo(final Description description) {
        description.appendList("(", " and ", ")", this.matchers);
    }
    
    @Factory
    public static <T> Matcher<T> allOf(final Iterable<Matcher<? super T>> matchers) {
        return new AllOf<T>(matchers);
    }
    
    @Factory
    public static <T> Matcher<T> allOf(final Matcher<? super T>... matchers) {
        return allOf((Iterable<Matcher<? super T>>)Arrays.asList(matchers));
    }
    
    @Factory
    public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second) {
        final List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>(2);
        matchers.add(first);
        matchers.add(second);
        return allOf((Iterable<Matcher<? super T>>)matchers);
    }
    
    @Factory
    public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third) {
        final List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>(3);
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        return allOf((Iterable<Matcher<? super T>>)matchers);
    }
    
    @Factory
    public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth) {
        final List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>(4);
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        matchers.add(fourth);
        return allOf((Iterable<Matcher<? super T>>)matchers);
    }
    
    @Factory
    public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth) {
        final List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>(5);
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        matchers.add(fourth);
        matchers.add(fifth);
        return allOf((Iterable<Matcher<? super T>>)matchers);
    }
    
    @Factory
    public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth, final Matcher<? super T> sixth) {
        final List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>(6);
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        matchers.add(fourth);
        matchers.add(fifth);
        matchers.add(sixth);
        return allOf((Iterable<Matcher<? super T>>)matchers);
    }
}
