// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runner;

import java.util.regex.Matcher;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.regex.Pattern;
import java.io.Serializable;

public class Description implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final Pattern METHOD_AND_CLASS_NAME_PATTERN;
    public static final Description EMPTY;
    public static final Description TEST_MECHANISM;
    private final Collection<Description> fChildren;
    private final String fDisplayName;
    private final Serializable fUniqueId;
    private final Annotation[] fAnnotations;
    private volatile Class<?> fTestClass;
    
    public static Description createSuiteDescription(final String name, final Annotation... annotations) {
        return new Description(null, name, annotations);
    }
    
    public static Description createSuiteDescription(final String name, final Serializable uniqueId, final Annotation... annotations) {
        return new Description(null, name, uniqueId, annotations);
    }
    
    public static Description createTestDescription(final String className, final String name, final Annotation... annotations) {
        return new Description(null, formatDisplayName(name, className), annotations);
    }
    
    public static Description createTestDescription(final Class<?> clazz, final String name, final Annotation... annotations) {
        return new Description(clazz, formatDisplayName(name, clazz.getName()), annotations);
    }
    
    public static Description createTestDescription(final Class<?> clazz, final String name) {
        return new Description(clazz, formatDisplayName(name, clazz.getName()), new Annotation[0]);
    }
    
    public static Description createTestDescription(final String className, final String name, final Serializable uniqueId) {
        return new Description(null, formatDisplayName(name, className), uniqueId, new Annotation[0]);
    }
    
    private static String formatDisplayName(final String name, final String className) {
        return String.format("%s(%s)", name, className);
    }
    
    public static Description createSuiteDescription(final Class<?> testClass) {
        return new Description(testClass, testClass.getName(), testClass.getAnnotations());
    }
    
    private Description(final Class<?> clazz, final String displayName, final Annotation... annotations) {
        this(clazz, displayName, displayName, annotations);
    }
    
    private Description(final Class<?> testClass, final String displayName, final Serializable uniqueId, final Annotation... annotations) {
        this.fChildren = new ConcurrentLinkedQueue<Description>();
        if (displayName == null || displayName.length() == 0) {
            throw new IllegalArgumentException("The display name must not be empty.");
        }
        if (uniqueId == null) {
            throw new IllegalArgumentException("The unique id must not be null.");
        }
        this.fTestClass = testClass;
        this.fDisplayName = displayName;
        this.fUniqueId = uniqueId;
        this.fAnnotations = annotations;
    }
    
    public String getDisplayName() {
        return this.fDisplayName;
    }
    
    public void addChild(final Description description) {
        this.fChildren.add(description);
    }
    
    public ArrayList<Description> getChildren() {
        return new ArrayList<Description>(this.fChildren);
    }
    
    public boolean isSuite() {
        return !this.isTest();
    }
    
    public boolean isTest() {
        return this.fChildren.isEmpty();
    }
    
    public int testCount() {
        if (this.isTest()) {
            return 1;
        }
        int result = 0;
        for (final Description child : this.fChildren) {
            result += child.testCount();
        }
        return result;
    }
    
    @Override
    public int hashCode() {
        return this.fUniqueId.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Description)) {
            return false;
        }
        final Description d = (Description)obj;
        return this.fUniqueId.equals(d.fUniqueId);
    }
    
    @Override
    public String toString() {
        return this.getDisplayName();
    }
    
    public boolean isEmpty() {
        return this.equals(Description.EMPTY);
    }
    
    public Description childlessCopy() {
        return new Description(this.fTestClass, this.fDisplayName, this.fAnnotations);
    }
    
    public <T extends Annotation> T getAnnotation(final Class<T> annotationType) {
        for (final Annotation each : this.fAnnotations) {
            if (each.annotationType().equals(annotationType)) {
                return annotationType.cast(each);
            }
        }
        return null;
    }
    
    public Collection<Annotation> getAnnotations() {
        return Arrays.asList(this.fAnnotations);
    }
    
    public Class<?> getTestClass() {
        if (this.fTestClass != null) {
            return this.fTestClass;
        }
        final String name = this.getClassName();
        if (name == null) {
            return null;
        }
        try {
            return this.fTestClass = Class.forName(name, false, this.getClass().getClassLoader());
        }
        catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    public String getClassName() {
        return (this.fTestClass != null) ? this.fTestClass.getName() : this.methodAndClassNamePatternGroupOrDefault(2, this.toString());
    }
    
    public String getMethodName() {
        return this.methodAndClassNamePatternGroupOrDefault(1, null);
    }
    
    private String methodAndClassNamePatternGroupOrDefault(final int group, final String defaultString) {
        final Matcher matcher = Description.METHOD_AND_CLASS_NAME_PATTERN.matcher(this.toString());
        return matcher.matches() ? matcher.group(group) : defaultString;
    }
    
    static {
        METHOD_AND_CLASS_NAME_PATTERN = Pattern.compile("([\\s\\S]*)\\((.*)\\)");
        EMPTY = new Description(null, "No Tests", new Annotation[0]);
        TEST_MECHANISM = new Description(null, "Test mechanism", new Annotation[0]);
    }
}
