// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest;

public abstract class BaseMatcher<T> implements Matcher<T>
{
    @Deprecated
    public final void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
    }
    
    public void describeMismatch(final Object item, final Description description) {
        description.appendText("was ").appendValue(item);
    }
    
    @Override
    public String toString() {
        return StringDescription.toString(this);
    }
}
