// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.theories.internal;

import java.lang.reflect.Constructor;
import org.junit.experimental.theories.ParametersSuppliedBy;
import org.junit.experimental.theories.ParameterSupplier;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.reflect.Method;
import org.junit.runners.model.TestClass;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.PotentialAssignment;
import java.util.List;

public class Assignments
{
    private final List<PotentialAssignment> assigned;
    private final List<ParameterSignature> unassigned;
    private final TestClass clazz;
    
    private Assignments(final List<PotentialAssignment> assigned, final List<ParameterSignature> unassigned, final TestClass clazz) {
        this.unassigned = unassigned;
        this.assigned = assigned;
        this.clazz = clazz;
    }
    
    public static Assignments allUnassigned(final Method testMethod, final TestClass testClass) {
        final List<ParameterSignature> signatures = ParameterSignature.signatures(testClass.getOnlyConstructor());
        signatures.addAll(ParameterSignature.signatures(testMethod));
        return new Assignments(new ArrayList<PotentialAssignment>(), signatures, testClass);
    }
    
    public boolean isComplete() {
        return this.unassigned.size() == 0;
    }
    
    public ParameterSignature nextUnassigned() {
        return this.unassigned.get(0);
    }
    
    public Assignments assignNext(final PotentialAssignment source) {
        final List<PotentialAssignment> assigned = new ArrayList<PotentialAssignment>(this.assigned);
        assigned.add(source);
        return new Assignments(assigned, this.unassigned.subList(1, this.unassigned.size()), this.clazz);
    }
    
    public Object[] getActualValues(final int start, final int stop) throws PotentialAssignment.CouldNotGenerateValueException {
        final Object[] values = new Object[stop - start];
        for (int i = start; i < stop; ++i) {
            values[i - start] = this.assigned.get(i).getValue();
        }
        return values;
    }
    
    public List<PotentialAssignment> potentialsForNextUnassigned() throws Throwable {
        final ParameterSignature unassigned = this.nextUnassigned();
        List<PotentialAssignment> assignments = this.getSupplier(unassigned).getValueSources(unassigned);
        if (assignments.size() == 0) {
            assignments = this.generateAssignmentsFromTypeAlone(unassigned);
        }
        return assignments;
    }
    
    private List<PotentialAssignment> generateAssignmentsFromTypeAlone(final ParameterSignature unassigned) {
        final Class<?> paramType = unassigned.getType();
        if (paramType.isEnum()) {
            return new EnumSupplier(paramType).getValueSources(unassigned);
        }
        if (paramType.equals(Boolean.class) || paramType.equals(Boolean.TYPE)) {
            return new BooleanSupplier().getValueSources(unassigned);
        }
        return Collections.emptyList();
    }
    
    private ParameterSupplier getSupplier(final ParameterSignature unassigned) throws Exception {
        final ParametersSuppliedBy annotation = unassigned.findDeepAnnotation(ParametersSuppliedBy.class);
        if (annotation != null) {
            return this.buildParameterSupplierFromClass(annotation.value());
        }
        return new AllMembersSupplier(this.clazz);
    }
    
    private ParameterSupplier buildParameterSupplierFromClass(final Class<? extends ParameterSupplier> cls) throws Exception {
        final Constructor[] arr$;
        final Constructor<?>[] supplierConstructors = (Constructor<?>[])(arr$ = cls.getConstructors());
        for (final Constructor<?> constructor : arr$) {
            final Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length == 1 && parameterTypes[0].equals(TestClass.class)) {
                return (ParameterSupplier)constructor.newInstance(this.clazz);
            }
        }
        return (ParameterSupplier)cls.newInstance();
    }
    
    public Object[] getConstructorArguments() throws PotentialAssignment.CouldNotGenerateValueException {
        return this.getActualValues(0, this.getConstructorParameterCount());
    }
    
    public Object[] getMethodArguments() throws PotentialAssignment.CouldNotGenerateValueException {
        return this.getActualValues(this.getConstructorParameterCount(), this.assigned.size());
    }
    
    public Object[] getAllArguments() throws PotentialAssignment.CouldNotGenerateValueException {
        return this.getActualValues(0, this.assigned.size());
    }
    
    private int getConstructorParameterCount() {
        final List<ParameterSignature> signatures = ParameterSignature.signatures(this.clazz.getOnlyConstructor());
        final int constructorParameterCount = signatures.size();
        return constructorParameterCount;
    }
    
    public Object[] getArgumentStrings(final boolean nullsOk) throws PotentialAssignment.CouldNotGenerateValueException {
        final Object[] values = new Object[this.assigned.size()];
        for (int i = 0; i < values.length; ++i) {
            values[i] = this.assigned.get(i).getDescription();
        }
        return values;
    }
}
