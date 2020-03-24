// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest;

import org.hamcrest.internal.ReflectiveTypeFinder;

public abstract class TypeSafeMatcher<T> extends BaseMatcher<T>
{
    private static final ReflectiveTypeFinder TYPE_FINDER;
    private final Class<?> expectedType;
    
    protected TypeSafeMatcher() {
        this(TypeSafeMatcher.TYPE_FINDER);
    }
    
    protected TypeSafeMatcher(final Class<?> expectedType) {
        this.expectedType = expectedType;
    }
    
    protected TypeSafeMatcher(final ReflectiveTypeFinder typeFinder) {
        this.expectedType = typeFinder.findExpectedType(this.getClass());
    }
    
    protected abstract boolean matchesSafely(final T p0);
    
    protected void describeMismatchSafely(final T item, final Description mismatchDescription) {
        super.describeMismatch(item, mismatchDescription);
    }
    
    public final boolean matches(final Object item) {
        return item != null && this.expectedType.isInstance(item) && this.matchesSafely(item);
    }
    
    @Override
    public final void describeMismatch(final Object item, final Description description) {
        if (item == null) {
            super.describeMismatch(item, description);
        }
        else if (!this.expectedType.isInstance(item)) {
            description.appendText("was a ").appendText(item.getClass().getName()).appendText(" (").appendValue(item).appendText(")");
        }
        else {
            this.describeMismatchSafely(item, description);
        }
    }
    
    static {
        TYPE_FINDER = new ReflectiveTypeFinder("matchesSafely", 1, 0);
    }
}
