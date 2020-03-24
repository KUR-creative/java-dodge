// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.internal;

import java.lang.reflect.Method;

public class ReflectiveTypeFinder
{
    private final String methodName;
    private final int expectedNumberOfParameters;
    private final int typedParameter;
    
    public ReflectiveTypeFinder(final String methodName, final int expectedNumberOfParameters, final int typedParameter) {
        this.methodName = methodName;
        this.expectedNumberOfParameters = expectedNumberOfParameters;
        this.typedParameter = typedParameter;
    }
    
    public Class<?> findExpectedType(final Class<?> fromClass) {
        for (Class<?> c = fromClass; c != Object.class; c = c.getSuperclass()) {
            for (final Method method : c.getDeclaredMethods()) {
                if (this.canObtainExpectedTypeFrom(method)) {
                    return this.expectedTypeFrom(method);
                }
            }
        }
        throw new Error("Cannot determine correct type for " + this.methodName + "() method.");
    }
    
    protected boolean canObtainExpectedTypeFrom(final Method method) {
        return method.getName().equals(this.methodName) && method.getParameterTypes().length == this.expectedNumberOfParameters && !method.isSynthetic();
    }
    
    protected Class<?> expectedTypeFrom(final Method method) {
        return method.getParameterTypes()[this.typedParameter];
    }
}
