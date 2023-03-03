// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.theories.internal;

import org.junit.runners.model.FrameworkMethod;
import org.junit.experimental.theories.DataPoints;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import org.junit.experimental.theories.DataPoint;
import java.util.ArrayList;
import org.junit.experimental.theories.FromDataPoints;
import java.lang.reflect.Field;
import java.util.Collection;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.runners.model.TestClass;

public class SpecificDataPointsSupplier extends AllMembersSupplier
{
    public SpecificDataPointsSupplier(final TestClass testClass) {
        super(testClass);
    }
    
    @Override
    protected Collection<Field> getSingleDataPointFields(final ParameterSignature sig) {
        final Collection<Field> fields = super.getSingleDataPointFields(sig);
        final String requestedName = sig.getAnnotation(FromDataPoints.class).value();
        final List<Field> fieldsWithMatchingNames = new ArrayList<Field>();
        for (final Field field : fields) {
            final String[] fieldNames = field.getAnnotation(DataPoint.class).value();
            if (Arrays.asList(fieldNames).contains(requestedName)) {
                fieldsWithMatchingNames.add(field);
            }
        }
        return fieldsWithMatchingNames;
    }
    
    @Override
    protected Collection<Field> getDataPointsFields(final ParameterSignature sig) {
        final Collection<Field> fields = super.getDataPointsFields(sig);
        final String requestedName = sig.getAnnotation(FromDataPoints.class).value();
        final List<Field> fieldsWithMatchingNames = new ArrayList<Field>();
        for (final Field field : fields) {
            final String[] fieldNames = field.getAnnotation(DataPoints.class).value();
            if (Arrays.asList(fieldNames).contains(requestedName)) {
                fieldsWithMatchingNames.add(field);
            }
        }
        return fieldsWithMatchingNames;
    }
    
    @Override
    protected Collection<FrameworkMethod> getSingleDataPointMethods(final ParameterSignature sig) {
        final Collection<FrameworkMethod> methods = super.getSingleDataPointMethods(sig);
        final String requestedName = sig.getAnnotation(FromDataPoints.class).value();
        final List<FrameworkMethod> methodsWithMatchingNames = new ArrayList<FrameworkMethod>();
        for (final FrameworkMethod method : methods) {
            final String[] methodNames = method.getAnnotation(DataPoint.class).value();
            if (Arrays.asList(methodNames).contains(requestedName)) {
                methodsWithMatchingNames.add(method);
            }
        }
        return methodsWithMatchingNames;
    }
    
    @Override
    protected Collection<FrameworkMethod> getDataPointsMethods(final ParameterSignature sig) {
        final Collection<FrameworkMethod> methods = super.getDataPointsMethods(sig);
        final String requestedName = sig.getAnnotation(FromDataPoints.class).value();
        final List<FrameworkMethod> methodsWithMatchingNames = new ArrayList<FrameworkMethod>();
        for (final FrameworkMethod method : methods) {
            final String[] methodNames = method.getAnnotation(DataPoints.class).value();
            if (Arrays.asList(methodNames).contains(requestedName)) {
                methodsWithMatchingNames.add(method);
            }
        }
        return methodsWithMatchingNames;
    }
}
