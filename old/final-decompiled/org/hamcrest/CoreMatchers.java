// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest;

import org.hamcrest.core.StringEndsWith;
import org.hamcrest.core.StringStartsWith;
import org.hamcrest.core.StringContains;
import org.hamcrest.core.IsSame;
import org.hamcrest.core.IsNull;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsCollectionContaining;
import org.hamcrest.core.IsAnything;
import org.hamcrest.core.Is;
import org.hamcrest.core.Every;
import org.hamcrest.core.DescribedAs;
import org.hamcrest.core.CombinableMatcher;
import org.hamcrest.core.AnyOf;
import org.hamcrest.core.AllOf;

public class CoreMatchers
{
    public static <T> Matcher<T> allOf(final Iterable<Matcher<? super T>> matchers) {
        return AllOf.allOf(matchers);
    }
    
    public static <T> Matcher<T> allOf(final Matcher<? super T>... matchers) {
        return AllOf.allOf(matchers);
    }
    
    public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second) {
        return AllOf.allOf(first, second);
    }
    
    public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third) {
        return AllOf.allOf(first, second, third);
    }
    
    public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth) {
        return AllOf.allOf(first, second, third, fourth);
    }
    
    public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth) {
        return AllOf.allOf(first, second, third, fourth, fifth);
    }
    
    public static <T> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth, final Matcher<? super T> sixth) {
        return AllOf.allOf(first, second, third, fourth, fifth, sixth);
    }
    
    public static <T> AnyOf<T> anyOf(final Iterable<Matcher<? super T>> matchers) {
        return AnyOf.anyOf(matchers);
    }
    
    public static <T> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third) {
        return AnyOf.anyOf(first, second, third);
    }
    
    public static <T> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth) {
        return AnyOf.anyOf(first, second, third, fourth);
    }
    
    public static <T> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth) {
        return AnyOf.anyOf(first, second, third, fourth, fifth);
    }
    
    public static <T> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth, final Matcher<? super T> sixth) {
        return AnyOf.anyOf(first, second, third, fourth, fifth, sixth);
    }
    
    public static <T> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second) {
        return AnyOf.anyOf(first, second);
    }
    
    public static <T> AnyOf<T> anyOf(final Matcher<? super T>... matchers) {
        return AnyOf.anyOf(matchers);
    }
    
    public static <LHS> CombinableMatcher.CombinableBothMatcher<LHS> both(final Matcher<? super LHS> matcher) {
        return CombinableMatcher.both(matcher);
    }
    
    public static <LHS> CombinableMatcher.CombinableEitherMatcher<LHS> either(final Matcher<? super LHS> matcher) {
        return CombinableMatcher.either(matcher);
    }
    
    public static <T> Matcher<T> describedAs(final String description, final Matcher<T> matcher, final Object... values) {
        return DescribedAs.describedAs(description, matcher, values);
    }
    
    public static <U> Matcher<Iterable<U>> everyItem(final Matcher<U> itemMatcher) {
        return Every.everyItem(itemMatcher);
    }
    
    public static <T> Matcher<T> is(final T value) {
        return Is.is(value);
    }
    
    public static <T> Matcher<T> is(final Matcher<T> matcher) {
        return Is.is(matcher);
    }
    
    @Deprecated
    public static <T> Matcher<T> is(final Class<T> type) {
        return Is.is(type);
    }
    
    public static <T> Matcher<T> isA(final Class<T> type) {
        return Is.isA(type);
    }
    
    public static Matcher<Object> anything() {
        return IsAnything.anything();
    }
    
    public static Matcher<Object> anything(final String description) {
        return IsAnything.anything(description);
    }
    
    public static <T> Matcher<Iterable<? super T>> hasItem(final T item) {
        return IsCollectionContaining.hasItem(item);
    }
    
    public static <T> Matcher<Iterable<? super T>> hasItem(final Matcher<? super T> itemMatcher) {
        return (Matcher<Iterable<? super T>>)IsCollectionContaining.hasItem((Matcher<? super Object>)itemMatcher);
    }
    
    public static <T> Matcher<Iterable<T>> hasItems(final T... items) {
        return IsCollectionContaining.hasItems(items);
    }
    
    public static <T> Matcher<Iterable<T>> hasItems(final Matcher<? super T>... itemMatchers) {
        return IsCollectionContaining.hasItems(itemMatchers);
    }
    
    public static <T> Matcher<T> equalTo(final T operand) {
        return IsEqual.equalTo(operand);
    }
    
    public static <T> Matcher<T> any(final Class<T> type) {
        return IsInstanceOf.any(type);
    }
    
    public static <T> Matcher<T> instanceOf(final Class<?> type) {
        return IsInstanceOf.instanceOf(type);
    }
    
    public static <T> Matcher<T> not(final Matcher<T> matcher) {
        return IsNot.not(matcher);
    }
    
    public static <T> Matcher<T> not(final T value) {
        return IsNot.not(value);
    }
    
    public static Matcher<Object> nullValue() {
        return IsNull.nullValue();
    }
    
    public static <T> Matcher<T> nullValue(final Class<T> type) {
        return IsNull.nullValue(type);
    }
    
    public static Matcher<Object> notNullValue() {
        return IsNull.notNullValue();
    }
    
    public static <T> Matcher<T> notNullValue(final Class<T> type) {
        return IsNull.notNullValue(type);
    }
    
    public static <T> Matcher<T> sameInstance(final T target) {
        return IsSame.sameInstance(target);
    }
    
    public static <T> Matcher<T> theInstance(final T target) {
        return IsSame.theInstance(target);
    }
    
    public static Matcher<String> containsString(final String substring) {
        return StringContains.containsString(substring);
    }
    
    public static Matcher<String> startsWith(final String prefix) {
        return StringStartsWith.startsWith(prefix);
    }
    
    public static Matcher<String> endsWith(final String suffix) {
        return StringEndsWith.endsWith(suffix);
    }
}
