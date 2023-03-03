// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest;

import java.io.IOException;

public class StringDescription extends BaseDescription
{
    private final Appendable out;
    
    public StringDescription() {
        this(new StringBuilder());
    }
    
    public StringDescription(final Appendable out) {
        this.out = out;
    }
    
    public static String toString(final SelfDescribing selfDescribing) {
        return new StringDescription().appendDescriptionOf(selfDescribing).toString();
    }
    
    public static String asString(final SelfDescribing selfDescribing) {
        return toString(selfDescribing);
    }
    
    @Override
    protected void append(final String str) {
        try {
            this.out.append(str);
        }
        catch (IOException e) {
            throw new RuntimeException("Could not write description", e);
        }
    }
    
    @Override
    protected void append(final char c) {
        try {
            this.out.append(c);
        }
        catch (IOException e) {
            throw new RuntimeException("Could not write description", e);
        }
    }
    
    @Override
    public String toString() {
        return this.out.toString();
    }
}
