// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest;

public abstract class DiagnosingMatcher<T> extends BaseMatcher<T>
{
    public final boolean matches(final Object item) {
        return this.matches(item, Description.NONE);
    }
    
    @Override
    public final void describeMismatch(final Object item, final Description mismatchDescription) {
        this.matches(item, mismatchDescription);
    }
    
    protected abstract boolean matches(final Object p0, final Description p1);
}
