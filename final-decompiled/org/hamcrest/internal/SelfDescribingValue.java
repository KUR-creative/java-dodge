// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.internal;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;

public class SelfDescribingValue<T> implements SelfDescribing
{
    private T value;
    
    public SelfDescribingValue(final T value) {
        this.value = value;
    }
    
    public void describeTo(final Description description) {
        description.appendValue(this.value);
    }
}
