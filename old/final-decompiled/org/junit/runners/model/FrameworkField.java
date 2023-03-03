// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FrameworkField extends FrameworkMember<FrameworkField>
{
    private final Field field;
    
    FrameworkField(final Field field) {
        if (field == null) {
            throw new NullPointerException("FrameworkField cannot be created without an underlying field.");
        }
        this.field = field;
    }
    
    @Override
    public String getName() {
        return this.getField().getName();
    }
    
    public Annotation[] getAnnotations() {
        return this.field.getAnnotations();
    }
    
    public <T extends Annotation> T getAnnotation(final Class<T> annotationType) {
        return this.field.getAnnotation(annotationType);
    }
    
    public boolean isShadowedBy(final FrameworkField otherMember) {
        return otherMember.getName().equals(this.getName());
    }
    
    @Override
    protected int getModifiers() {
        return this.field.getModifiers();
    }
    
    public Field getField() {
        return this.field;
    }
    
    @Override
    public Class<?> getType() {
        return this.field.getType();
    }
    
    @Override
    public Class<?> getDeclaringClass() {
        return this.field.getDeclaringClass();
    }
    
    public Object get(final Object target) throws IllegalArgumentException, IllegalAccessException {
        return this.field.get(target);
    }
    
    @Override
    public String toString() {
        return this.field.toString();
    }
}
