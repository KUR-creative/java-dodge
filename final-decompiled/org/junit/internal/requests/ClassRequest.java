// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.requests;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Runner;
import org.junit.runner.Request;

public class ClassRequest extends Request
{
    private final Object runnerLock;
    private final Class<?> fTestClass;
    private final boolean canUseSuiteMethod;
    private volatile Runner runner;
    
    public ClassRequest(final Class<?> testClass, final boolean canUseSuiteMethod) {
        this.runnerLock = new Object();
        this.fTestClass = testClass;
        this.canUseSuiteMethod = canUseSuiteMethod;
    }
    
    public ClassRequest(final Class<?> testClass) {
        this(testClass, true);
    }
    
    @Override
    public Runner getRunner() {
        if (this.runner == null) {
            synchronized (this.runnerLock) {
                if (this.runner == null) {
                    this.runner = new AllDefaultPossibilitiesBuilder(this.canUseSuiteMethod).safeRunnerForClass(this.fTestClass);
                }
            }
        }
        return this.runner;
    }
}
