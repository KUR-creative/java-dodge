// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners;

import java.util.Iterator;
import java.lang.reflect.Modifier;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import java.lang.reflect.Method;
import org.junit.Test;
import org.junit.Before;
import java.lang.annotation.Annotation;
import org.junit.After;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class MethodValidator
{
    private final List<Throwable> errors;
    private TestClass testClass;
    
    public MethodValidator(final TestClass testClass) {
        this.errors = new ArrayList<Throwable>();
        this.testClass = testClass;
    }
    
    public void validateInstanceMethods() {
        this.validateTestMethods(After.class, false);
        this.validateTestMethods(Before.class, false);
        this.validateTestMethods(Test.class, false);
        final List<Method> methods = this.testClass.getAnnotatedMethods(Test.class);
        if (methods.size() == 0) {
            this.errors.add(new Exception("No runnable methods"));
        }
    }
    
    public void validateStaticMethods() {
        this.validateTestMethods(BeforeClass.class, true);
        this.validateTestMethods(AfterClass.class, true);
    }
    
    public List<Throwable> validateMethodsForDefaultRunner() {
        this.validateNoArgConstructor();
        this.validateStaticMethods();
        this.validateInstanceMethods();
        return this.errors;
    }
    
    public void assertValid() throws InitializationError {
        if (!this.errors.isEmpty()) {
            throw new InitializationError(this.errors);
        }
    }
    
    public void validateNoArgConstructor() {
        try {
            this.testClass.getConstructor();
        }
        catch (Exception e) {
            this.errors.add(new Exception("Test class should have public zero-argument constructor", e));
        }
    }
    
    private void validateTestMethods(final Class<? extends Annotation> annotation, final boolean isStatic) {
        final List<Method> methods = this.testClass.getAnnotatedMethods(annotation);
        for (final Method each : methods) {
            if (Modifier.isStatic(each.getModifiers()) != isStatic) {
                final String state = isStatic ? "should" : "should not";
                this.errors.add(new Exception("Method " + each.getName() + "() " + state + " be static"));
            }
            if (!Modifier.isPublic(each.getDeclaringClass().getModifiers())) {
                this.errors.add(new Exception("Class " + each.getDeclaringClass().getName() + " should be public"));
            }
            if (!Modifier.isPublic(each.getModifiers())) {
                this.errors.add(new Exception("Method " + each.getName() + " should be public"));
            }
            if (each.getReturnType() != Void.TYPE) {
                this.errors.add(new Exception("Method " + each.getName() + " should be void"));
            }
            if (each.getParameterTypes().length != 0) {
                this.errors.add(new Exception("Method " + each.getName() + " should have no parameters"));
            }
        }
    }
}
