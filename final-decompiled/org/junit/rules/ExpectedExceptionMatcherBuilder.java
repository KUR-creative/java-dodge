// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.rules;

import java.util.Collection;
import org.hamcrest.CoreMatchers;
import org.junit.matchers.JUnitMatchers;
import java.util.ArrayList;
import org.hamcrest.Matcher;
import java.util.List;

class ExpectedExceptionMatcherBuilder
{
    private final List<Matcher<?>> matchers;
    
    ExpectedExceptionMatcherBuilder() {
        this.matchers = new ArrayList<Matcher<?>>();
    }
    
    void add(final Matcher<?> matcher) {
        this.matchers.add(matcher);
    }
    
    boolean expectsThrowable() {
        return !this.matchers.isEmpty();
    }
    
    Matcher<Throwable> build() {
        return JUnitMatchers.isThrowable(this.allOfTheMatchers());
    }
    
    private Matcher<Throwable> allOfTheMatchers() {
        if (this.matchers.size() == 1) {
            return this.cast(this.matchers.get(0));
        }
        return CoreMatchers.allOf((Iterable<Matcher<? super Throwable>>)this.castedMatchers());
    }
    
    private List<Matcher<? super Throwable>> castedMatchers() {
        return new ArrayList<Matcher<? super Throwable>>((Collection<? extends Matcher<? super Throwable>>)this.matchers);
    }
    
    private Matcher<Throwable> cast(final Matcher<?> singleMatcher) {
        return (Matcher<Throwable>)singleMatcher;
    }
}
