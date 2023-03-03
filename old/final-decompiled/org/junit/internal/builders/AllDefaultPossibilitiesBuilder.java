// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.builders;

import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

public class AllDefaultPossibilitiesBuilder extends RunnerBuilder
{
    private final boolean canUseSuiteMethod;
    
    public AllDefaultPossibilitiesBuilder(final boolean canUseSuiteMethod) {
        this.canUseSuiteMethod = canUseSuiteMethod;
    }
    
    @Override
    public Runner runnerForClass(final Class<?> testClass) throws Throwable {
        final List<RunnerBuilder> builders = Arrays.asList(this.ignoredBuilder(), this.annotatedBuilder(), this.suiteMethodBuilder(), this.junit3Builder(), this.junit4Builder());
        for (final RunnerBuilder each : builders) {
            final Runner runner = each.safeRunnerForClass(testClass);
            if (runner != null) {
                return runner;
            }
        }
        return null;
    }
    
    protected JUnit4Builder junit4Builder() {
        return new JUnit4Builder();
    }
    
    protected JUnit3Builder junit3Builder() {
        return new JUnit3Builder();
    }
    
    protected AnnotatedBuilder annotatedBuilder() {
        return new AnnotatedBuilder(this);
    }
    
    protected IgnoredBuilder ignoredBuilder() {
        return new IgnoredBuilder();
    }
    
    protected RunnerBuilder suiteMethodBuilder() {
        if (this.canUseSuiteMethod) {
            return new SuiteMethodBuilder();
        }
        return new NullBuilder();
    }
}
