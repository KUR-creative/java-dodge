// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.theories.internal;

import org.junit.Assume;
import org.junit.runners.model.FrameworkField;
import org.junit.experimental.theories.DataPoint;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Iterator;
import org.junit.experimental.theories.DataPoints;
import org.junit.runners.model.FrameworkMethod;
import java.util.ArrayList;
import org.junit.experimental.theories.PotentialAssignment;
import java.util.List;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.runners.model.TestClass;
import org.junit.experimental.theories.ParameterSupplier;

public class AllMembersSupplier extends ParameterSupplier
{
    private final TestClass clazz;
    
    public AllMembersSupplier(final TestClass type) {
        this.clazz = type;
    }
    
    @Override
    public List<PotentialAssignment> getValueSources(final ParameterSignature sig) throws Throwable {
        final List<PotentialAssignment> list = new ArrayList<PotentialAssignment>();
        this.addSinglePointFields(sig, list);
        this.addMultiPointFields(sig, list);
        this.addSinglePointMethods(sig, list);
        this.addMultiPointMethods(sig, list);
        return list;
    }
    
    private void addMultiPointMethods(final ParameterSignature sig, final List<PotentialAssignment> list) throws Throwable {
        for (final FrameworkMethod dataPointsMethod : this.getDataPointsMethods(sig)) {
            final Class<?> returnType = dataPointsMethod.getReturnType();
            if (!returnType.isArray() || !sig.canPotentiallyAcceptType(returnType.getComponentType())) {
                if (!Iterable.class.isAssignableFrom(returnType)) {
                    continue;
                }
            }
            try {
                this.addDataPointsValues(returnType, sig, dataPointsMethod.getName(), list, dataPointsMethod.invokeExplosively(null, new Object[0]));
            }
            catch (Throwable throwable) {
                final DataPoints annotation = dataPointsMethod.getAnnotation(DataPoints.class);
                if (annotation != null && isAssignableToAnyOf(annotation.ignoredExceptions(), throwable)) {
                    return;
                }
                throw throwable;
            }
        }
    }
    
    private void addSinglePointMethods(final ParameterSignature sig, final List<PotentialAssignment> list) {
        for (final FrameworkMethod dataPointMethod : this.getSingleDataPointMethods(sig)) {
            if (sig.canAcceptType(dataPointMethod.getType())) {
                list.add(new MethodParameterValue(dataPointMethod));
            }
        }
    }
    
    private void addMultiPointFields(final ParameterSignature sig, final List<PotentialAssignment> list) {
        for (final Field field : this.getDataPointsFields(sig)) {
            final Class<?> type = field.getType();
            this.addDataPointsValues(type, sig, field.getName(), list, this.getStaticFieldValue(field));
        }
    }
    
    private void addSinglePointFields(final ParameterSignature sig, final List<PotentialAssignment> list) {
        for (final Field field : this.getSingleDataPointFields(sig)) {
            final Object value = this.getStaticFieldValue(field);
            if (sig.canAcceptValue(value)) {
                list.add(PotentialAssignment.forValue(field.getName(), value));
            }
        }
    }
    
    private void addDataPointsValues(final Class<?> type, final ParameterSignature sig, final String name, final List<PotentialAssignment> list, final Object value) {
        if (type.isArray()) {
            this.addArrayValues(sig, name, list, value);
        }
        else if (Iterable.class.isAssignableFrom(type)) {
            this.addIterableValues(sig, name, list, (Iterable<?>)value);
        }
    }
    
    private void addArrayValues(final ParameterSignature sig, final String name, final List<PotentialAssignment> list, final Object array) {
        for (int i = 0; i < Array.getLength(array); ++i) {
            final Object value = Array.get(array, i);
            if (sig.canAcceptValue(value)) {
                list.add(PotentialAssignment.forValue(name + "[" + i + "]", value));
            }
        }
    }
    
    private void addIterableValues(final ParameterSignature sig, final String name, final List<PotentialAssignment> list, final Iterable<?> iterable) {
        final Iterator<?> iterator = iterable.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            final Object value = iterator.next();
            if (sig.canAcceptValue(value)) {
                list.add(PotentialAssignment.forValue(name + "[" + i + "]", value));
            }
            ++i;
        }
    }
    
    private Object getStaticFieldValue(final Field field) {
        try {
            return field.get(null);
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("unexpected: field from getClass doesn't exist on object");
        }
        catch (IllegalAccessException e2) {
            throw new RuntimeException("unexpected: getFields returned an inaccessible field");
        }
    }
    
    private static boolean isAssignableToAnyOf(final Class<?>[] typeArray, final Object target) {
        for (final Class<?> type : typeArray) {
            if (type.isAssignableFrom(target.getClass())) {
                return true;
            }
        }
        return false;
    }
    
    protected Collection<FrameworkMethod> getDataPointsMethods(final ParameterSignature sig) {
        return this.clazz.getAnnotatedMethods(DataPoints.class);
    }
    
    protected Collection<Field> getSingleDataPointFields(final ParameterSignature sig) {
        final List<FrameworkField> fields = this.clazz.getAnnotatedFields(DataPoint.class);
        final Collection<Field> validFields = new ArrayList<Field>();
        for (final FrameworkField frameworkField : fields) {
            validFields.add(frameworkField.getField());
        }
        return validFields;
    }
    
    protected Collection<Field> getDataPointsFields(final ParameterSignature sig) {
        final List<FrameworkField> fields = this.clazz.getAnnotatedFields(DataPoints.class);
        final Collection<Field> validFields = new ArrayList<Field>();
        for (final FrameworkField frameworkField : fields) {
            validFields.add(frameworkField.getField());
        }
        return validFields;
    }
    
    protected Collection<FrameworkMethod> getSingleDataPointMethods(final ParameterSignature sig) {
        return this.clazz.getAnnotatedMethods(DataPoint.class);
    }
    
    static class MethodParameterValue extends PotentialAssignment
    {
        private final FrameworkMethod method;
        
        private MethodParameterValue(final FrameworkMethod dataPointMethod) {
            this.method = dataPointMethod;
        }
        
        @Override
        public Object getValue() throws CouldNotGenerateValueException {
            try {
                return this.method.invokeExplosively(null, new Object[0]);
            }
            catch (IllegalArgumentException e) {
                throw new RuntimeException("unexpected: argument length is checked");
            }
            catch (IllegalAccessException e2) {
                throw new RuntimeException("unexpected: getMethods returned an inaccessible method");
            }
            catch (Throwable throwable) {
                final DataPoint annotation = this.method.getAnnotation(DataPoint.class);
                Assume.assumeTrue(annotation == null || !isAssignableToAnyOf(annotation.ignoredExceptions(), throwable));
                throw new CouldNotGenerateValueException(throwable);
            }
        }
        
        @Override
        public String getDescription() throws CouldNotGenerateValueException {
            return this.method.getName();
        }
    }
}
