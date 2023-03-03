// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.theories.internal;

import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;

public class ParameterizedAssertionError extends AssertionError
{
    private static final long serialVersionUID = 1L;
    
    public ParameterizedAssertionError(final Throwable targetException, final String methodName, final Object... params) {
        super((Object)String.format("%s(%s)", methodName, join(", ", params)));
        this.initCause(targetException);
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof ParameterizedAssertionError && this.toString().equals(obj.toString());
    }
    
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
    
    public static String join(final String delimiter, final Object... params) {
        return join(delimiter, Arrays.asList(params));
    }
    
    public static String join(final String delimiter, final Collection<Object> values) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<Object> iter = values.iterator();
        while (iter.hasNext()) {
            final Object next = iter.next();
            sb.append(stringValueOf(next));
            if (iter.hasNext()) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }
    
    private static String stringValueOf(final Object next) {
        try {
            return String.valueOf(next);
        }
        catch (Throwable e) {
            return "[toString failed]";
        }
    }
}
