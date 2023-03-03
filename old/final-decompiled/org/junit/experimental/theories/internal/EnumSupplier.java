// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.theories.internal;

import java.util.ArrayList;
import org.junit.experimental.theories.PotentialAssignment;
import java.util.List;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;

public class EnumSupplier extends ParameterSupplier
{
    private Class<?> enumType;
    
    public EnumSupplier(final Class<?> enumType) {
        this.enumType = enumType;
    }
    
    @Override
    public List<PotentialAssignment> getValueSources(final ParameterSignature sig) {
        final Object[] enumValues = (Object[])this.enumType.getEnumConstants();
        final List<PotentialAssignment> assignments = new ArrayList<PotentialAssignment>();
        for (final Object value : enumValues) {
            assignments.add(PotentialAssignment.forValue(value.toString(), value));
        }
        return assignments;
    }
}
