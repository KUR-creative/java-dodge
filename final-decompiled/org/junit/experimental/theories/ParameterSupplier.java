// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.theories;

import java.util.List;

public abstract class ParameterSupplier
{
    public abstract List<PotentialAssignment> getValueSources(final ParameterSignature p0) throws Throwable;
}
