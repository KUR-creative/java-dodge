// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runner.manipulation;

import org.junit.runner.Description;
import java.util.Comparator;

public class Sorter implements Comparator<Description>
{
    public static final Sorter NULL;
    private final Comparator<Description> comparator;
    
    public Sorter(final Comparator<Description> comparator) {
        this.comparator = comparator;
    }
    
    public void apply(final Object object) {
        if (object instanceof Sortable) {
            final Sortable sortable = (Sortable)object;
            sortable.sort(this);
        }
    }
    
    public int compare(final Description o1, final Description o2) {
        return this.comparator.compare(o1, o2);
    }
    
    static {
        NULL = new Sorter(new Comparator<Description>() {
            public int compare(final Description o1, final Description o2) {
                return 0;
            }
        });
    }
}
