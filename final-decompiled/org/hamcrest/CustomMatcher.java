// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest;

public abstract class CustomMatcher<T> extends BaseMatcher<T>
{
    private final String fixedDescription;
    
    public CustomMatcher(final String description) {
        if (description == null) {
            throw new IllegalArgumentException("Description should be non null!");
        }
        this.fixedDescription = description;
    }
    
    public final void describeTo(final Description description) {
        description.appendText(this.fixedDescription);
    }
}
