// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest;

public interface Matcher<T> extends SelfDescribing
{
    boolean matches(final Object p0);
    
    void describeMismatch(final Object p0, final Description p1);
    
    @Deprecated
    void _dont_implement_Matcher___instead_extend_BaseMatcher_();
}
