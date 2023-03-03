// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.hamcrest.Factory;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class AnyOf<T> extends ShortcutCombination<T>
{
    public AnyOf(final Iterable<Matcher<? super T>> matchers) {
        super(matchers);
    }
    
    @Override
    public boolean matches(final Object o) {
        return this.matches(o, true);
    }
    
    @Override
    public void describeTo(final Description description) {
        this.describeTo(description, "or");
    }
    
    @Factory
    public static <T> AnyOf<T> anyOf(final Iterable<Matcher<? super T>> matchers) {
        return new AnyOf<T>(matchers);
    }
    
    @Factory
    public static <T> AnyOf<T> anyOf(final Matcher<? super T>... matchers) {
        return anyOf((Iterable<Matcher<? super T>>)Arrays.asList(matchers));
    }
    
    @Factory
    public static <T> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second) {
        final List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();
        matchers.add(first);
        matchers.add(second);
        return anyOf((Iterable<Matcher<? super T>>)matchers);
    }
    
    @Factory
    public static <T> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third) {
        final List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        return anyOf((Iterable<Matcher<? super T>>)matchers);
    }
    
    @Factory
    public static <T> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth) {
        final List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        matchers.add(fourth);
        return anyOf((Iterable<Matcher<? super T>>)matchers);
    }
    
    @Factory
    public static <T> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth) {
        final List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        matchers.add(fourth);
        matchers.add(fifth);
        return anyOf((Iterable<Matcher<? super T>>)matchers);
    }
    
    @Factory
    public static <T> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth, final Matcher<? super T> sixth) {
        final List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        matchers.add(fourth);
        matchers.add(fifth);
        matchers.add(sixth);
        return anyOf((Iterable<Matcher<? super T>>)matchers);
    }
}
