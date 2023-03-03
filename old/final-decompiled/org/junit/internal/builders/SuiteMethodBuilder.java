// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.builders;

import org.junit.internal.runners.SuiteMethod;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

public class SuiteMethodBuilder extends RunnerBuilder
{
    @Override
    public Runner runnerForClass(final Class<?> each) throws Throwable {
        if (this.hasSuiteMethod(each)) {
            return new SuiteMethod(each);
        }
        return null;
    }
    
    public boolean hasSuiteMethod(final Class<?> testClass) {
        try {
            testClass.getMethod("suite", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }
}
