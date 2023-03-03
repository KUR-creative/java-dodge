// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import org.hamcrest.SelfDescribing;
import java.util.Iterator;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.BaseMatcher;

abstract class ShortcutCombination<T> extends BaseMatcher<T>
{
    private final Iterable<Matcher<? super T>> matchers;
    
    public ShortcutCombination(final Iterable<Matcher<? super T>> matchers) {
        this.matchers = matchers;
    }
    
    public abstract boolean matches(final Object p0);
    
    public abstract void describeTo(final Description p0);
    
    protected boolean matches(final Object o, final boolean shortcut) {
        for (final Matcher<? super T> matcher : this.matchers) {
            if (matcher.matches(o) == shortcut) {
                return shortcut;
            }
        }
        return !shortcut;
    }
    
    public void describeTo(final Description description, final String operator) {
        description.appendList("(", " " + operator + " ", ")", this.matchers);
    }
}
