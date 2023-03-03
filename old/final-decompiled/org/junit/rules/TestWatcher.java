// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.rules;

import org.junit.runners.model.MultipleFailureException;
import org.junit.internal.AssumptionViolatedException;
import java.util.List;
import java.util.ArrayList;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public abstract class TestWatcher implements TestRule
{
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final List<Throwable> errors = new ArrayList<Throwable>();
                TestWatcher.this.startingQuietly(description, errors);
                try {
                    base.evaluate();
                    TestWatcher.this.succeededQuietly(description, errors);
                }
                catch (AssumptionViolatedException e) {
                    errors.add(e);
                    TestWatcher.this.skippedQuietly(e, description, errors);
                }
                catch (Throwable e2) {
                    errors.add(e2);
                    TestWatcher.this.failedQuietly(e2, description, errors);
                }
                finally {
                    TestWatcher.this.finishedQuietly(description, errors);
                }
                MultipleFailureException.assertEmpty(errors);
            }
        };
    }
    
    private void succeededQuietly(final Description description, final List<Throwable> errors) {
        try {
            this.succeeded(description);
        }
        catch (Throwable e) {
            errors.add(e);
        }
    }
    
    private void failedQuietly(final Throwable e, final Description description, final List<Throwable> errors) {
        try {
            this.failed(e, description);
        }
        catch (Throwable e2) {
            errors.add(e2);
        }
    }
    
    private void skippedQuietly(final AssumptionViolatedException e, final Description description, final List<Throwable> errors) {
        try {
            if (e instanceof org.junit.AssumptionViolatedException) {
                this.skipped((org.junit.AssumptionViolatedException)e, description);
            }
            else {
                this.skipped(e, description);
            }
        }
        catch (Throwable e2) {
            errors.add(e2);
        }
    }
    
    private void startingQuietly(final Description description, final List<Throwable> errors) {
        try {
            this.starting(description);
        }
        catch (Throwable e) {
            errors.add(e);
        }
    }
    
    private void finishedQuietly(final Description description, final List<Throwable> errors) {
        try {
            this.finished(description);
        }
        catch (Throwable e) {
            errors.add(e);
        }
    }
    
    protected void succeeded(final Description description) {
    }
    
    protected void failed(final Throwable e, final Description description) {
    }
    
    protected void skipped(final org.junit.AssumptionViolatedException e, final Description description) {
        final AssumptionViolatedException asInternalException = e;
        this.skipped(asInternalException, description);
    }
    
    @Deprecated
    protected void skipped(final AssumptionViolatedException e, final Description description) {
    }
    
    protected void starting(final Description description) {
    }
    
    protected void finished(final Description description) {
    }
}
