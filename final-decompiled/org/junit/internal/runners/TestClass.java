// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners;

import java.lang.reflect.Constructor;
import org.junit.Before;
import java.util.Iterator;
import java.util.Collections;
import org.junit.internal.MethodSorter;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import java.lang.annotation.Annotation;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.List;

@Deprecated
public class TestClass
{
    private final Class<?> klass;
    
    public TestClass(final Class<?> klass) {
        this.klass = klass;
    }
    
    public List<Method> getTestMethods() {
        return this.getAnnotatedMethods(Test.class);
    }
    
    List<Method> getBefores() {
        return this.getAnnotatedMethods(BeforeClass.class);
    }
    
    List<Method> getAfters() {
        return this.getAnnotatedMethods(AfterClass.class);
    }
    
    public List<Method> getAnnotatedMethods(final Class<? extends Annotation> annotationClass) {
        final List<Method> results = new ArrayList<Method>();
        for (final Class<?> eachClass : this.getSuperClasses(this.klass)) {
            final Method[] arr$;
            final Method[] methods = arr$ = MethodSorter.getDeclaredMethods(eachClass);
            for (final Method eachMethod : arr$) {
                final Annotation annotation = eachMethod.getAnnotation(annotationClass);
                if (annotation != null && !this.isShadowed(eachMethod, results)) {
                    results.add(eachMethod);
                }
            }
        }
        if (this.runsTopToBottom(annotationClass)) {
            Collections.reverse(results);
        }
        return results;
    }
    
    private boolean runsTopToBottom(final Class<? extends Annotation> annotation) {
        return annotation.equals(Before.class) || annotation.equals(BeforeClass.class);
    }
    
    private boolean isShadowed(final Method method, final List<Method> results) {
        for (final Method each : results) {
            if (this.isShadowed(method, each)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isShadowed(final Method current, final Method previous) {
        if (!previous.getName().equals(current.getName())) {
            return false;
        }
        if (previous.getParameterTypes().length != current.getParameterTypes().length) {
            return false;
        }
        for (int i = 0; i < previous.getParameterTypes().length; ++i) {
            if (!previous.getParameterTypes()[i].equals(current.getParameterTypes()[i])) {
                return false;
            }
        }
        return true;
    }
    
    private List<Class<?>> getSuperClasses(final Class<?> testClass) {
        final ArrayList<Class<?>> results = new ArrayList<Class<?>>();
        for (Class<?> current = testClass; current != null; current = current.getSuperclass()) {
            results.add(current);
        }
        return results;
    }
    
    public Constructor<?> getConstructor() throws SecurityException, NoSuchMethodException {
        return this.klass.getConstructor((Class<?>[])new Class[0]);
    }
    
    public Class<?> getJavaClass() {
        return this.klass;
    }
    
    public String getName() {
        return this.klass.getName();
    }
}
