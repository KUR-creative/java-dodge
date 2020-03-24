// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners;

import org.junit.runner.notification.Failure;
import java.util.Arrays;
import org.junit.runners.model.InitializationError;
import java.lang.reflect.InvocationTargetException;
import org.junit.runner.notification.RunNotifier;
import java.util.Iterator;
import org.junit.runner.Description;
import java.util.List;
import org.junit.runner.Runner;

public class ErrorReportingRunner extends Runner
{
    private final List<Throwable> causes;
    private final Class<?> testClass;
    
    public ErrorReportingRunner(final Class<?> testClass, final Throwable cause) {
        if (testClass == null) {
            throw new NullPointerException("Test class cannot be null");
        }
        this.testClass = testClass;
        this.causes = this.getCauses(cause);
    }
    
    @Override
    public Description getDescription() {
        final Description description = Description.createSuiteDescription(this.testClass);
        for (final Throwable each : this.causes) {
            description.addChild(this.describeCause(each));
        }
        return description;
    }
    
    @Override
    public void run(final RunNotifier notifier) {
        for (final Throwable each : this.causes) {
            this.runCause(each, notifier);
        }
    }
    
    private List<Throwable> getCauses(final Throwable cause) {
        if (cause instanceof InvocationTargetException) {
            return this.getCauses(cause.getCause());
        }
        if (cause instanceof InitializationError) {
            return ((InitializationError)cause).getCauses();
        }
        if (cause instanceof org.junit.internal.runners.InitializationError) {
            return ((org.junit.internal.runners.InitializationError)cause).getCauses();
        }
        return Arrays.asList(cause);
    }
    
    private Description describeCause(final Throwable child) {
        return Description.createTestDescription(this.testClass, "initializationError");
    }
    
    private void runCause(final Throwable child, final RunNotifier notifier) {
        final Description description = this.describeCause(child);
        notifier.fireTestStarted(description);
        notifier.fireTestFailure(new Failure(description, child));
        notifier.fireTestFinished(description);
    }
}
