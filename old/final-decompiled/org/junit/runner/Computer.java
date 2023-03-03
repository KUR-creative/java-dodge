// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runner;

import org.junit.runners.model.InitializationError;
import org.junit.runners.Suite;
import org.junit.runners.model.RunnerBuilder;

public class Computer
{
    public static Computer serial() {
        return new Computer();
    }
    
    public Runner getSuite(final RunnerBuilder builder, final Class<?>[] classes) throws InitializationError {
        return new Suite(new RunnerBuilder() {
            @Override
            public Runner runnerForClass(final Class<?> testClass) throws Throwable {
                return Computer.this.getRunner(builder, testClass);
            }
        }, classes);
    }
    
    protected Runner getRunner(final RunnerBuilder builder, final Class<?> testClass) throws Throwable {
        return builder.runnerForClass(testClass);
    }
}
