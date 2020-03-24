// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners.model;

import java.util.ArrayList;
import java.util.List;
import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.Runner;
import java.util.HashSet;
import java.util.Set;

public abstract class RunnerBuilder
{
    private final Set<Class<?>> parents;
    
    public RunnerBuilder() {
        this.parents = new HashSet<Class<?>>();
    }
    
    public abstract Runner runnerForClass(final Class<?> p0) throws Throwable;
    
    public Runner safeRunnerForClass(final Class<?> testClass) {
        try {
            return this.runnerForClass(testClass);
        }
        catch (Throwable e) {
            return new ErrorReportingRunner(testClass, e);
        }
    }
    
    Class<?> addParent(final Class<?> parent) throws InitializationError {
        if (!this.parents.add(parent)) {
            throw new InitializationError(String.format("class '%s' (possibly indirectly) contains itself as a SuiteClass", parent.getName()));
        }
        return parent;
    }
    
    void removeParent(final Class<?> klass) {
        this.parents.remove(klass);
    }
    
    public List<Runner> runners(final Class<?> parent, final Class<?>[] children) throws InitializationError {
        this.addParent(parent);
        try {
            return this.runners(children);
        }
        finally {
            this.removeParent(parent);
        }
    }
    
    public List<Runner> runners(final Class<?> parent, final List<Class<?>> children) throws InitializationError {
        return this.runners(parent, children.toArray(new Class[0]));
    }
    
    private List<Runner> runners(final Class<?>[] children) {
        final ArrayList<Runner> runners = new ArrayList<Runner>();
        for (final Class<?> each : children) {
            final Runner childRunner = this.safeRunnerForClass(each);
            if (childRunner != null) {
                runners.add(childRunner);
            }
        }
        return runners;
    }
}
