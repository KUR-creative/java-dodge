// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners.model;

import java.lang.reflect.Modifier;
import org.junit.Assert;
import java.lang.reflect.Constructor;
import org.junit.BeforeClass;
import org.junit.Before;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Arrays;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import org.junit.internal.MethodSorter;
import java.util.LinkedHashMap;
import java.util.List;
import java.lang.annotation.Annotation;
import java.util.Map;

public class TestClass implements Annotatable
{
    private static final FieldComparator FIELD_COMPARATOR;
    private static final MethodComparator METHOD_COMPARATOR;
    private final Class<?> clazz;
    private final Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations;
    private final Map<Class<? extends Annotation>, List<FrameworkField>> fieldsForAnnotations;
    
    public TestClass(final Class<?> clazz) {
        this.clazz = clazz;
        if (clazz != null && clazz.getConstructors().length > 1) {
            throw new IllegalArgumentException("Test class can only have one constructor");
        }
        final Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations = new LinkedHashMap<Class<? extends Annotation>, List<FrameworkMethod>>();
        final Map<Class<? extends Annotation>, List<FrameworkField>> fieldsForAnnotations = new LinkedHashMap<Class<? extends Annotation>, List<FrameworkField>>();
        this.scanAnnotatedMembers(methodsForAnnotations, fieldsForAnnotations);
        this.methodsForAnnotations = makeDeeplyUnmodifiable(methodsForAnnotations);
        this.fieldsForAnnotations = makeDeeplyUnmodifiable(fieldsForAnnotations);
    }
    
    protected void scanAnnotatedMembers(final Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations, final Map<Class<? extends Annotation>, List<FrameworkField>> fieldsForAnnotations) {
        for (final Class<?> eachClass : getSuperClasses(this.clazz)) {
            for (final Method eachMethod : MethodSorter.getDeclaredMethods(eachClass)) {
                addToAnnotationLists(new FrameworkMethod(eachMethod), methodsForAnnotations);
            }
            for (final Field eachField : getSortedDeclaredFields(eachClass)) {
                addToAnnotationLists(new FrameworkField(eachField), fieldsForAnnotations);
            }
        }
    }
    
    private static Field[] getSortedDeclaredFields(final Class<?> clazz) {
        final Field[] declaredFields = clazz.getDeclaredFields();
        Arrays.sort(declaredFields, TestClass.FIELD_COMPARATOR);
        return declaredFields;
    }
    
    protected static <T extends FrameworkMember<T>> void addToAnnotationLists(final T member, final Map<Class<? extends Annotation>, List<T>> map) {
        for (final Annotation each : member.getAnnotations()) {
            final Class<? extends Annotation> type = each.annotationType();
            final List<T> members = getAnnotatedMembers(map, type, true);
            if (member.isShadowedBy(members)) {
                return;
            }
            if (runsTopToBottom(type)) {
                members.add(0, member);
            }
            else {
                members.add(member);
            }
        }
    }
    
    private static <T extends FrameworkMember<T>> Map<Class<? extends Annotation>, List<T>> makeDeeplyUnmodifiable(final Map<Class<? extends Annotation>, List<T>> source) {
        final LinkedHashMap<Class<? extends Annotation>, List<T>> copy = new LinkedHashMap<Class<? extends Annotation>, List<T>>();
        for (final Map.Entry<Class<? extends Annotation>, List<T>> entry : source.entrySet()) {
            copy.put(entry.getKey(), Collections.unmodifiableList((List<? extends T>)entry.getValue()));
        }
        return Collections.unmodifiableMap((Map<? extends Class<? extends Annotation>, ? extends List<T>>)copy);
    }
    
    public List<FrameworkMethod> getAnnotatedMethods() {
        final List<FrameworkMethod> methods = this.collectValues(this.methodsForAnnotations);
        Collections.sort(methods, TestClass.METHOD_COMPARATOR);
        return methods;
    }
    
    public List<FrameworkMethod> getAnnotatedMethods(final Class<? extends Annotation> annotationClass) {
        return Collections.unmodifiableList((List<? extends FrameworkMethod>)getAnnotatedMembers((Map<Class<? extends Annotation>, List<? extends T>>)this.methodsForAnnotations, annotationClass, false));
    }
    
    public List<FrameworkField> getAnnotatedFields() {
        return this.collectValues(this.fieldsForAnnotations);
    }
    
    public List<FrameworkField> getAnnotatedFields(final Class<? extends Annotation> annotationClass) {
        return Collections.unmodifiableList((List<? extends FrameworkField>)getAnnotatedMembers((Map<Class<? extends Annotation>, List<? extends T>>)this.fieldsForAnnotations, annotationClass, false));
    }
    
    private <T> List<T> collectValues(final Map<?, List<T>> map) {
        final Set<T> values = new LinkedHashSet<T>();
        for (final List<T> additionalValues : map.values()) {
            values.addAll((Collection<? extends T>)additionalValues);
        }
        return new ArrayList<T>((Collection<? extends T>)values);
    }
    
    private static <T> List<T> getAnnotatedMembers(final Map<Class<? extends Annotation>, List<T>> map, final Class<? extends Annotation> type, final boolean fillIfAbsent) {
        if (!map.containsKey(type) && fillIfAbsent) {
            map.put(type, new ArrayList<T>());
        }
        final List<T> members = map.get(type);
        return (members == null) ? Collections.emptyList() : members;
    }
    
    private static boolean runsTopToBottom(final Class<? extends Annotation> annotation) {
        return annotation.equals(Before.class) || annotation.equals(BeforeClass.class);
    }
    
    private static List<Class<?>> getSuperClasses(final Class<?> testClass) {
        final ArrayList<Class<?>> results = new ArrayList<Class<?>>();
        for (Class<?> current = testClass; current != null; current = current.getSuperclass()) {
            results.add(current);
        }
        return results;
    }
    
    public Class<?> getJavaClass() {
        return this.clazz;
    }
    
    public String getName() {
        if (this.clazz == null) {
            return "null";
        }
        return this.clazz.getName();
    }
    
    public Constructor<?> getOnlyConstructor() {
        final Constructor<?>[] constructors = this.clazz.getConstructors();
        Assert.assertEquals(1L, constructors.length);
        return constructors[0];
    }
    
    public Annotation[] getAnnotations() {
        if (this.clazz == null) {
            return new Annotation[0];
        }
        return this.clazz.getAnnotations();
    }
    
    public <T extends Annotation> T getAnnotation(final Class<T> annotationType) {
        if (this.clazz == null) {
            return null;
        }
        return this.clazz.getAnnotation(annotationType);
    }
    
    public <T> List<T> getAnnotatedFieldValues(final Object test, final Class<? extends Annotation> annotationClass, final Class<T> valueClass) {
        final List<T> results = new ArrayList<T>();
        for (final FrameworkField each : this.getAnnotatedFields(annotationClass)) {
            try {
                final Object fieldValue = each.get(test);
                if (!valueClass.isInstance(fieldValue)) {
                    continue;
                }
                results.add(valueClass.cast(fieldValue));
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException("How did getFields return a field we couldn't access?", e);
            }
        }
        return results;
    }
    
    public <T> List<T> getAnnotatedMethodValues(final Object test, final Class<? extends Annotation> annotationClass, final Class<T> valueClass) {
        final List<T> results = new ArrayList<T>();
        for (final FrameworkMethod each : this.getAnnotatedMethods(annotationClass)) {
            try {
                if (!valueClass.isAssignableFrom(each.getReturnType())) {
                    continue;
                }
                final Object fieldValue = each.invokeExplosively(test, new Object[0]);
                results.add(valueClass.cast(fieldValue));
            }
            catch (Throwable e) {
                throw new RuntimeException("Exception in " + each.getName(), e);
            }
        }
        return results;
    }
    
    public boolean isPublic() {
        return Modifier.isPublic(this.clazz.getModifiers());
    }
    
    public boolean isANonStaticInnerClass() {
        return this.clazz.isMemberClass() && !Modifier.isStatic(this.clazz.getModifiers());
    }
    
    @Override
    public int hashCode() {
        return (this.clazz == null) ? 0 : this.clazz.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final TestClass other = (TestClass)obj;
        return this.clazz == other.clazz;
    }
    
    static {
        FIELD_COMPARATOR = new FieldComparator();
        METHOD_COMPARATOR = new MethodComparator();
    }
    
    private static class FieldComparator implements Comparator<Field>
    {
        public int compare(final Field left, final Field right) {
            return left.getName().compareTo(right.getName());
        }
    }
    
    private static class MethodComparator implements Comparator<FrameworkMethod>
    {
        public int compare(final FrameworkMethod left, final FrameworkMethod right) {
            return MethodSorter.NAME_ASCENDING.compare(left.getMethod(), right.getMethod());
        }
    }
}
