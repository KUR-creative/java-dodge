// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.theories;

import java.util.Iterator;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.lang.annotation.Annotation;
import java.util.Map;

public class ParameterSignature
{
    private static final Map<Class<?>, Class<?>> CONVERTABLE_TYPES_MAP;
    private final Class<?> type;
    private final Annotation[] annotations;
    
    private static Map<Class<?>, Class<?>> buildConvertableTypesMap() {
        final Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
        putSymmetrically(map, Boolean.TYPE, Boolean.class);
        putSymmetrically(map, Byte.TYPE, Byte.class);
        putSymmetrically(map, Short.TYPE, Short.class);
        putSymmetrically(map, Character.TYPE, Character.class);
        putSymmetrically(map, Integer.TYPE, Integer.class);
        putSymmetrically(map, Long.TYPE, Long.class);
        putSymmetrically(map, Float.TYPE, Float.class);
        putSymmetrically(map, Double.TYPE, Double.class);
        return Collections.unmodifiableMap((Map<? extends Class<?>, ? extends Class<?>>)map);
    }
    
    private static <T> void putSymmetrically(final Map<T, T> map, final T a, final T b) {
        map.put(a, b);
        map.put(b, a);
    }
    
    public static ArrayList<ParameterSignature> signatures(final Method method) {
        return signatures(method.getParameterTypes(), method.getParameterAnnotations());
    }
    
    public static List<ParameterSignature> signatures(final Constructor<?> constructor) {
        return signatures(constructor.getParameterTypes(), constructor.getParameterAnnotations());
    }
    
    private static ArrayList<ParameterSignature> signatures(final Class<?>[] parameterTypes, final Annotation[][] parameterAnnotations) {
        final ArrayList<ParameterSignature> sigs = new ArrayList<ParameterSignature>();
        for (int i = 0; i < parameterTypes.length; ++i) {
            sigs.add(new ParameterSignature(parameterTypes[i], parameterAnnotations[i]));
        }
        return sigs;
    }
    
    private ParameterSignature(final Class<?> type, final Annotation[] annotations) {
        this.type = type;
        this.annotations = annotations;
    }
    
    public boolean canAcceptValue(final Object candidate) {
        return (candidate == null) ? (!this.type.isPrimitive()) : this.canAcceptType(candidate.getClass());
    }
    
    public boolean canAcceptType(final Class<?> candidate) {
        return this.type.isAssignableFrom(candidate) || this.isAssignableViaTypeConversion(this.type, candidate);
    }
    
    public boolean canPotentiallyAcceptType(final Class<?> candidate) {
        return candidate.isAssignableFrom(this.type) || this.isAssignableViaTypeConversion(candidate, this.type) || this.canAcceptType(candidate);
    }
    
    private boolean isAssignableViaTypeConversion(final Class<?> targetType, final Class<?> candidate) {
        if (ParameterSignature.CONVERTABLE_TYPES_MAP.containsKey(candidate)) {
            final Class<?> wrapperClass = ParameterSignature.CONVERTABLE_TYPES_MAP.get(candidate);
            return targetType.isAssignableFrom(wrapperClass);
        }
        return false;
    }
    
    public Class<?> getType() {
        return this.type;
    }
    
    public List<Annotation> getAnnotations() {
        return Arrays.asList(this.annotations);
    }
    
    public boolean hasAnnotation(final Class<? extends Annotation> type) {
        return this.getAnnotation(type) != null;
    }
    
    public <T extends Annotation> T findDeepAnnotation(final Class<T> annotationType) {
        final Annotation[] annotations2 = this.annotations;
        return this.findDeepAnnotation(annotations2, annotationType, 3);
    }
    
    private <T extends Annotation> T findDeepAnnotation(final Annotation[] annotations, final Class<T> annotationType, final int depth) {
        if (depth == 0) {
            return null;
        }
        for (final Annotation each : annotations) {
            if (annotationType.isInstance(each)) {
                return annotationType.cast(each);
            }
            final Annotation candidate = this.findDeepAnnotation(each.annotationType().getAnnotations(), (Class<Annotation>)annotationType, depth - 1);
            if (candidate != null) {
                return annotationType.cast(candidate);
            }
        }
        return null;
    }
    
    public <T extends Annotation> T getAnnotation(final Class<T> annotationType) {
        for (final Annotation each : this.getAnnotations()) {
            if (annotationType.isInstance(each)) {
                return annotationType.cast(each);
            }
        }
        return null;
    }
    
    static {
        CONVERTABLE_TYPES_MAP = buildConvertableTypesMap();
    }
}
