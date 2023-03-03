// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.internal;

import org.hamcrest.SelfDescribing;
import java.util.Iterator;

public class SelfDescribingValueIterator<T> implements Iterator<SelfDescribing>
{
    private Iterator<T> values;
    
    public SelfDescribingValueIterator(final Iterator<T> values) {
        this.values = values;
    }
    
    public boolean hasNext() {
        return this.values.hasNext();
    }
    
    public SelfDescribing next() {
        return new SelfDescribingValue<Object>(this.values.next());
    }
    
    public void remove() {
        this.values.remove();
    }
}
