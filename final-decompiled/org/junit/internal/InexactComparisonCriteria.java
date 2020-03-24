// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal;

import org.junit.Assert;

public class InexactComparisonCriteria extends ComparisonCriteria
{
    public Object fDelta;
    
    public InexactComparisonCriteria(final double delta) {
        this.fDelta = delta;
    }
    
    public InexactComparisonCriteria(final float delta) {
        this.fDelta = delta;
    }
    
    @Override
    protected void assertElementsEqual(final Object expected, final Object actual) {
        if (expected instanceof Double) {
            Assert.assertEquals((double)expected, (double)actual, (double)this.fDelta);
        }
        else {
            Assert.assertEquals((float)expected, (float)actual, (float)this.fDelta);
        }
    }
}
