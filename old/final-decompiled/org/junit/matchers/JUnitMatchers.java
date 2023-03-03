// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.matchers;

import org.junit.internal.matchers.StacktracePrintingMatcher;
import org.hamcrest.core.CombinableMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

public class JUnitMatchers
{
    @Deprecated
    public static <T> Matcher<Iterable<? super T>> hasItem(final T element) {
        return CoreMatchers.hasItem(element);
    }
    
    @Deprecated
    public static <T> Matcher<Iterable<? super T>> hasItem(final Matcher<? super T> elementMatcher) {
        return (Matcher<Iterable<? super T>>)CoreMatchers.hasItem((Matcher<? super Object>)elementMatcher);
    }
    
    @Deprecated
    public static <T> Matcher<Iterable<T>> hasItems(final T... elements) {
        return CoreMatchers.hasItems(elements);
    }
    
    @Deprecated
    public static <T> Matcher<Iterable<T>> hasItems(final Matcher<? super T>... elementMatchers) {
        return CoreMatchers.hasItems(elementMatchers);
    }
    
    @Deprecated
    public static <T> Matcher<Iterable<T>> everyItem(final Matcher<T> elementMatcher) {
        return CoreMatchers.everyItem(elementMatcher);
    }
    
    @Deprecated
    public static Matcher<String> containsString(final String substring) {
        return CoreMatchers.containsString(substring);
    }
    
    @Deprecated
    public static <T> CombinableMatcher.CombinableBothMatcher<T> both(final Matcher<? super T> matcher) {
        return CoreMatchers.both(matcher);
    }
    
    @Deprecated
    public static <T> CombinableMatcher.CombinableEitherMatcher<T> either(final Matcher<? super T> matcher) {
        return CoreMatchers.either(matcher);
    }
    
    public static <T extends Throwable> Matcher<T> isThrowable(final Matcher<T> throwableMatcher) {
        return StacktracePrintingMatcher.isThrowable(throwableMatcher);
    }
    
    public static <T extends Exception> Matcher<T> isException(final Matcher<T> exceptionMatcher) {
        return StacktracePrintingMatcher.isException(exceptionMatcher);
    }
}
