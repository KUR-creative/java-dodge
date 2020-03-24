// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import org.hamcrest.Factory;
import java.util.ArrayList;
import org.hamcrest.SelfDescribing;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class CombinableMatcher<T> extends TypeSafeDiagnosingMatcher<T>
{
    private final Matcher<? super T> matcher;
    
    public CombinableMatcher(final Matcher<? super T> matcher) {
        this.matcher = matcher;
    }
    
    @Override
    protected boolean matchesSafely(final T item, final Description mismatch) {
        if (!this.matcher.matches(item)) {
            this.matcher.describeMismatch(item, mismatch);
            return false;
        }
        return true;
    }
    
    public void describeTo(final Description description) {
        description.appendDescriptionOf(this.matcher);
    }
    
    public CombinableMatcher<T> and(final Matcher<? super T> other) {
        return new CombinableMatcher<T>(new AllOf<Object>(this.templatedListWith(other)));
    }
    
    public CombinableMatcher<T> or(final Matcher<? super T> other) {
        return new CombinableMatcher<T>(new AnyOf<Object>(this.templatedListWith(other)));
    }
    
    private ArrayList<Matcher<? super T>> templatedListWith(final Matcher<? super T> other) {
        final ArrayList<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();
        matchers.add(this.matcher);
        matchers.add(other);
        return matchers;
    }
    
    @Factory
    public static <LHS> CombinableBothMatcher<LHS> both(final Matcher<? super LHS> matcher) {
        return new CombinableBothMatcher<LHS>(matcher);
    }
    
    @Factory
    public static <LHS> CombinableEitherMatcher<LHS> either(final Matcher<? super LHS> matcher) {
        return new CombinableEitherMatcher<LHS>(matcher);
    }
    
    public static final class CombinableBothMatcher<X>
    {
        private final Matcher<? super X> first;
        
        public CombinableBothMatcher(final Matcher<? super X> matcher) {
            this.first = matcher;
        }
        
        public CombinableMatcher<X> and(final Matcher<? super X> other) {
            return new CombinableMatcher<X>(this.first).and(other);
        }
    }
    
    public static final class CombinableEitherMatcher<X>
    {
        private final Matcher<? super X> first;
        
        public CombinableEitherMatcher(final Matcher<? super X> matcher) {
            this.first = matcher;
        }
        
        public CombinableMatcher<X> or(final Matcher<? super X> other) {
            return new CombinableMatcher<X>(this.first).or(other);
        }
    }
}
