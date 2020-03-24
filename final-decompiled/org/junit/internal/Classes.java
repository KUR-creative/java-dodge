// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal;

public class Classes
{
    public static Class<?> getClass(final String className) throws ClassNotFoundException {
        return Class.forName(className, true, Thread.currentThread().getContextClassLoader());
    }
}
