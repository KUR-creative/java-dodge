// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest;

import org.hamcrest.internal.ReflectiveTypeFinder;

public abstract class TypeSafeDiagnosingMatcher<T> extends BaseMatcher<T>
{
    private static final ReflectiveTypeFinder TYPE_FINDER;
    private final Class<?> expectedType;
    
    protected abstract boolean matchesSafely(final T p0, final Description p1);
    
    protected TypeSafeDiagnosingMatcher(final Class<?> expectedType) {
        this.expectedType = expectedType;
    }
    
    protected TypeSafeDiagnosingMatcher(final ReflectiveTypeFinder typeFinder) {
        this.expectedType = typeFinder.findExpectedType(this.getClass());
    }
    
    protected TypeSafeDiagnosingMatcher() {
        this(TypeSafeDiagnosingMatcher.TYPE_FINDER);
    }
    
    public final boolean matches(final Object item) {
        return item != null && this.expectedType.isInstance(item) && this.matchesSafely(item, new Description.NullDescription());
    }
    
    @Override
    public final void describeMismatch(final Object item, final Description mismatchDescription) {
        if (item == null || !this.expectedType.isInstance(item)) {
            super.describeMismatch(item, mismatchDescription);
        }
        else {
            this.matchesSafely(item, mismatchDescription);
        }
    }
    
    static {
        TYPE_FINDER = new ReflectiveTypeFinder("matchesSafely", 2, 0);
    }
}
