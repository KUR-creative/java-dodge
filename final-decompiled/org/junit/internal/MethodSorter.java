// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal;

import java.util.Arrays;
import org.junit.FixMethodOrder;
import java.lang.reflect.Method;
import java.util.Comparator;

public class MethodSorter
{
    public static final Comparator<Method> DEFAULT;
    public static final Comparator<Method> NAME_ASCENDING;
    
    public static Method[] getDeclaredMethods(final Class<?> clazz) {
        final Comparator<Method> comparator = getSorter(clazz.getAnnotation(FixMethodOrder.class));
        final Method[] methods = clazz.getDeclaredMethods();
        if (comparator != null) {
            Arrays.sort(methods, comparator);
        }
        return methods;
    }
    
    private MethodSorter() {
    }
    
    private static Comparator<Method> getSorter(final FixMethodOrder fixMethodOrder) {
        if (fixMethodOrder == null) {
            return MethodSorter.DEFAULT;
        }
        return fixMethodOrder.value().getComparator();
    }
    
    static {
        DEFAULT = new Comparator<Method>() {
            public int compare(final Method m1, final Method m2) {
                final int i1 = m1.getName().hashCode();
                final int i2 = m2.getName().hashCode();
                if (i1 != i2) {
                    return (i1 < i2) ? -1 : 1;
                }
                return MethodSorter.NAME_ASCENDING.compare(m1, m2);
            }
        };
        NAME_ASCENDING = new Comparator<Method>() {
            public int compare(final Method m1, final Method m2) {
                final int comparison = m1.getName().compareTo(m2.getName());
                if (comparison != 0) {
                    return comparison;
                }
                return m1.toString().compareTo(m2.toString());
            }
        };
    }
}
