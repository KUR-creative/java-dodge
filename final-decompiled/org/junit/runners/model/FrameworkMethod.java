// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import org.junit.internal.runners.model.ReflectiveCallable;
import java.lang.reflect.Method;

public class FrameworkMethod extends FrameworkMember<FrameworkMethod>
{
    private final Method method;
    
    public FrameworkMethod(final Method method) {
        if (method == null) {
            throw new NullPointerException("FrameworkMethod cannot be created without an underlying method.");
        }
        this.method = method;
    }
    
    public Method getMethod() {
        return this.method;
    }
    
    public Object invokeExplosively(final Object target, final Object... params) throws Throwable {
        return new ReflectiveCallable() {
            @Override
            protected Object runReflectiveCall() throws Throwable {
                return FrameworkMethod.this.method.invoke(target, params);
            }
        }.run();
    }
    
    @Override
    public String getName() {
        return this.method.getName();
    }
    
    public void validatePublicVoidNoArg(final boolean isStatic, final List<Throwable> errors) {
        this.validatePublicVoid(isStatic, errors);
        if (this.method.getParameterTypes().length != 0) {
            errors.add(new Exception("Method " + this.method.getName() + " should have no parameters"));
        }
    }
    
    public void validatePublicVoid(final boolean isStatic, final List<Throwable> errors) {
        if (this.isStatic() != isStatic) {
            final String state = isStatic ? "should" : "should not";
            errors.add(new Exception("Method " + this.method.getName() + "() " + state + " be static"));
        }
        if (!this.isPublic()) {
            errors.add(new Exception("Method " + this.method.getName() + "() should be public"));
        }
        if (this.method.getReturnType() != Void.TYPE) {
            errors.add(new Exception("Method " + this.method.getName() + "() should be void"));
        }
    }
    
    @Override
    protected int getModifiers() {
        return this.method.getModifiers();
    }
    
    public Class<?> getReturnType() {
        return this.method.getReturnType();
    }
    
    @Override
    public Class<?> getType() {
        return this.getReturnType();
    }
    
    @Override
    public Class<?> getDeclaringClass() {
        return this.method.getDeclaringClass();
    }
    
    public void validateNoTypeParametersOnArgs(final List<Throwable> errors) {
        new NoGenericTypeParametersValidator(this.method).validate(errors);
    }
    
    public boolean isShadowedBy(final FrameworkMethod other) {
        if (!other.getName().equals(this.getName())) {
            return false;
        }
        if (other.getParameterTypes().length != this.getParameterTypes().length) {
            return false;
        }
        for (int i = 0; i < other.getParameterTypes().length; ++i) {
            if (!other.getParameterTypes()[i].equals(this.getParameterTypes()[i])) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return FrameworkMethod.class.isInstance(obj) && ((FrameworkMethod)obj).method.equals(this.method);
    }
    
    @Override
    public int hashCode() {
        return this.method.hashCode();
    }
    
    @Deprecated
    public boolean producesType(final Type type) {
        return this.getParameterTypes().length == 0 && type instanceof Class && ((Class)type).isAssignableFrom(this.method.getReturnType());
    }
    
    private Class<?>[] getParameterTypes() {
        return this.method.getParameterTypes();
    }
    
    public Annotation[] getAnnotations() {
        return this.method.getAnnotations();
    }
    
    public <T extends Annotation> T getAnnotation(final Class<T> annotationType) {
        return this.method.getAnnotation(annotationType);
    }
    
    @Override
    public String toString() {
        return this.method.toString();
    }
}
