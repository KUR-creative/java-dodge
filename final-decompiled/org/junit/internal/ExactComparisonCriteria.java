// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal;

import org.junit.Assert;

public class ExactComparisonCriteria extends ComparisonCriteria
{
    @Override
    protected void assertElementsEqual(final Object expected, final Object actual) {
        Assert.assertEquals(expected, actual);
    }
}
