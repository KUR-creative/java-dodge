// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class ArrayComparisonFailure extends AssertionError
{
    private static final long serialVersionUID = 1L;
    private final List<Integer> fIndices;
    private final String fMessage;
    
    public ArrayComparisonFailure(final String message, final AssertionError cause, final int index) {
        this.fIndices = new ArrayList<Integer>();
        this.fMessage = message;
        this.initCause(cause);
        this.addDimension(index);
    }
    
    public void addDimension(final int index) {
        this.fIndices.add(0, index);
    }
    
    @Override
    public String getMessage() {
        final StringBuilder sb = new StringBuilder();
        if (this.fMessage != null) {
            sb.append(this.fMessage);
        }
        sb.append("arrays first differed at element ");
        for (final int each : this.fIndices) {
            sb.append("[");
            sb.append(each);
            sb.append("]");
        }
        sb.append("; ");
        sb.append(this.getCause().getMessage());
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.getMessage();
    }
}
