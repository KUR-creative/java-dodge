// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners.model;

import org.junit.internal.AssumptionViolatedException;
import java.util.Iterator;
import org.junit.runner.notification.Failure;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

public class EachTestNotifier
{
    private final RunNotifier notifier;
    private final Description description;
    
    public EachTestNotifier(final RunNotifier notifier, final Description description) {
        this.notifier = notifier;
        this.description = description;
    }
    
    public void addFailure(final Throwable targetException) {
        if (targetException instanceof MultipleFailureException) {
            this.addMultipleFailureException((MultipleFailureException)targetException);
        }
        else {
            this.notifier.fireTestFailure(new Failure(this.description, targetException));
        }
    }
    
    private void addMultipleFailureException(final MultipleFailureException mfe) {
        for (final Throwable each : mfe.getFailures()) {
            this.addFailure(each);
        }
    }
    
    public void addFailedAssumption(final AssumptionViolatedException e) {
        this.notifier.fireTestAssumptionFailed(new Failure(this.description, e));
    }
    
    public void fireTestFinished() {
        this.notifier.fireTestFinished(this.description);
    }
    
    public void fireTestStarted() {
        this.notifier.fireTestStarted(this.description);
    }
    
    public void fireTestIgnored() {
        this.notifier.fireTestIgnored(this.description);
    }
}
