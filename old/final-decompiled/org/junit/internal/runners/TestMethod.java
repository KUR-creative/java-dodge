// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners;

import java.lang.reflect.InvocationTargetException;
import org.junit.After;
import java.lang.annotation.Annotation;
import org.junit.Before;
import java.util.List;
import org.junit.Test;
import org.junit.Ignore;
import java.lang.reflect.Method;

@Deprecated
public class TestMethod
{
    private final Method method;
    private TestClass testClass;
    
    public TestMethod(final Method method, final TestClass testClass) {
        this.method = method;
        this.testClass = testClass;
    }
    
    public boolean isIgnored() {
        return this.method.getAnnotation(Ignore.class) != null;
    }
    
    public long getTimeout() {
        final Test annotation = this.method.getAnnotation(Test.class);
        if (annotation == null) {
            return 0L;
        }
        final long timeout = annotation.timeout();
        return timeout;
    }
    
    protected Class<? extends Throwable> getExpectedException() {
        final Test annotation = this.method.getAnnotation(Test.class);
        if (annotation == null || annotation.expected() == Test.None.class) {
            return null;
        }
        return annotation.expected();
    }
    
    boolean isUnexpected(final Throwable exception) {
        return !this.getExpectedException().isAssignableFrom(exception.getClass());
    }
    
    boolean expectsException() {
        return this.getExpectedException() != null;
    }
    
    List<Method> getBefores() {
        return this.testClass.getAnnotatedMethods(Before.class);
    }
    
    List<Method> getAfters() {
        return this.testClass.getAnnotatedMethods(After.class);
    }
    
    public void invoke(final Object test) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        this.method.invoke(test, new Object[0]);
    }
}
