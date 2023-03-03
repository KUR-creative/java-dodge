// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners.model;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.WildcardType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Type;
import java.util.List;
import java.lang.reflect.Method;

class NoGenericTypeParametersValidator
{
    private final Method method;
    
    NoGenericTypeParametersValidator(final Method method) {
        this.method = method;
    }
    
    void validate(final List<Throwable> errors) {
        for (final Type each : this.method.getGenericParameterTypes()) {
            this.validateNoTypeParameterOnType(each, errors);
        }
    }
    
    private void validateNoTypeParameterOnType(final Type type, final List<Throwable> errors) {
        if (type instanceof TypeVariable) {
            errors.add(new Exception("Method " + this.method.getName() + "() contains unresolved type variable " + type));
        }
        else if (type instanceof ParameterizedType) {
            this.validateNoTypeParameterOnParameterizedType((ParameterizedType)type, errors);
        }
        else if (type instanceof WildcardType) {
            this.validateNoTypeParameterOnWildcardType((WildcardType)type, errors);
        }
        else if (type instanceof GenericArrayType) {
            this.validateNoTypeParameterOnGenericArrayType((GenericArrayType)type, errors);
        }
    }
    
    private void validateNoTypeParameterOnParameterizedType(final ParameterizedType parameterized, final List<Throwable> errors) {
        for (final Type each : parameterized.getActualTypeArguments()) {
            this.validateNoTypeParameterOnType(each, errors);
        }
    }
    
    private void validateNoTypeParameterOnWildcardType(final WildcardType wildcard, final List<Throwable> errors) {
        for (final Type each : wildcard.getUpperBounds()) {
            this.validateNoTypeParameterOnType(each, errors);
        }
        for (final Type each : wildcard.getLowerBounds()) {
            this.validateNoTypeParameterOnType(each, errors);
        }
    }
    
    private void validateNoTypeParameterOnGenericArrayType(final GenericArrayType arrayType, final List<Throwable> errors) {
        this.validateNoTypeParameterOnType(arrayType.getGenericComponentType(), errors);
    }
}
