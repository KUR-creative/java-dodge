// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.theories.suppliers;

import java.util.ArrayList;
import org.junit.experimental.theories.PotentialAssignment;
import java.util.List;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;

public class TestedOnSupplier extends ParameterSupplier
{
    @Override
    public List<PotentialAssignment> getValueSources(final ParameterSignature sig) {
        final List<PotentialAssignment> list = new ArrayList<PotentialAssignment>();
        final TestedOn testedOn = sig.getAnnotation(TestedOn.class);
        final int[] arr$;
        final int[] ints = arr$ = testedOn.ints();
        for (final int i : arr$) {
            list.add(PotentialAssignment.forValue("ints", i));
        }
        return list;
    }
}
