// 
// Decompiled by Procyon v0.5.36
// 

package org.junit;

import org.hamcrest.Matcher;

public class AssumptionViolatedException extends org.junit.internal.AssumptionViolatedException
{
    private static final long serialVersionUID = 1L;
    
    public <T> AssumptionViolatedException(final T actual, final Matcher<T> matcher) {
        super(actual, matcher);
    }
    
    public <T> AssumptionViolatedException(final String message, final T expected, final Matcher<T> matcher) {
        super(message, expected, matcher);
    }
    
    public AssumptionViolatedException(final String message) {
        super(message);
    }
    
    public AssumptionViolatedException(final String assumption, final Throwable t) {
        super(assumption, t);
    }
}
