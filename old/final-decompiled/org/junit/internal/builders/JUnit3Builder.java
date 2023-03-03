// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.builders;

import junit.framework.TestCase;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

public class JUnit3Builder extends RunnerBuilder
{
    @Override
    public Runner runnerForClass(final Class<?> testClass) throws Throwable {
        if (this.isPre4Test(testClass)) {
            return new JUnit38ClassRunner(testClass);
        }
        return null;
    }
    
    boolean isPre4Test(final Class<?> testClass) {
        return TestCase.class.isAssignableFrom(testClass);
    }
}
