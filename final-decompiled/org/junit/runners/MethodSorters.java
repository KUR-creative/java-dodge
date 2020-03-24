// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners;

import org.junit.internal.MethodSorter;
import java.lang.reflect.Method;
import java.util.Comparator;

public enum MethodSorters
{
    NAME_ASCENDING(MethodSorter.NAME_ASCENDING), 
    JVM((Comparator<Method>)null), 
    DEFAULT(MethodSorter.DEFAULT);
    
    private final Comparator<Method> comparator;
    
    private MethodSorters(final Comparator<Method> comparator) {
        this.comparator = comparator;
    }
    
    public Comparator<Method> getComparator() {
        return this.comparator;
    }
}
