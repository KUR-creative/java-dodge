// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.results;

import java.util.Iterator;
import org.junit.runner.notification.RunListener;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import java.util.List;

class FailureList
{
    private final List<Failure> failures;
    
    public FailureList(final List<Failure> failures) {
        this.failures = failures;
    }
    
    public Result result() {
        final Result result = new Result();
        final RunListener listener = result.createListener();
        for (final Failure failure : this.failures) {
            try {
                listener.testFailure(failure);
            }
            catch (Exception e) {
                throw new RuntimeException("I can't believe this happened");
            }
        }
        return result;
    }
}
