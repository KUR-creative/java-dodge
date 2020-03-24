// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runner.notification;

import org.junit.runner.Result;
import org.junit.runner.Description;

@ThreadSafe
final class SynchronizedRunListener extends RunListener
{
    private final RunListener listener;
    private final Object monitor;
    
    SynchronizedRunListener(final RunListener listener, final Object monitor) {
        this.listener = listener;
        this.monitor = monitor;
    }
    
    @Override
    public void testRunStarted(final Description description) throws Exception {
        synchronized (this.monitor) {
            this.listener.testRunStarted(description);
        }
    }
    
    @Override
    public void testRunFinished(final Result result) throws Exception {
        synchronized (this.monitor) {
            this.listener.testRunFinished(result);
        }
    }
    
    @Override
    public void testStarted(final Description description) throws Exception {
        synchronized (this.monitor) {
            this.listener.testStarted(description);
        }
    }
    
    @Override
    public void testFinished(final Description description) throws Exception {
        synchronized (this.monitor) {
            this.listener.testFinished(description);
        }
    }
    
    @Override
    public void testFailure(final Failure failure) throws Exception {
        synchronized (this.monitor) {
            this.listener.testFailure(failure);
        }
    }
    
    @Override
    public void testAssumptionFailure(final Failure failure) {
        synchronized (this.monitor) {
            this.listener.testAssumptionFailure(failure);
        }
    }
    
    @Override
    public void testIgnored(final Description description) throws Exception {
        synchronized (this.monitor) {
            this.listener.testIgnored(description);
        }
    }
    
    @Override
    public int hashCode() {
        return this.listener.hashCode();
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SynchronizedRunListener)) {
            return false;
        }
        final SynchronizedRunListener that = (SynchronizedRunListener)other;
        return this.listener.equals(that.listener);
    }
    
    @Override
    public String toString() {
        return this.listener.toString() + " (with synchronization wrapper)";
    }
}
