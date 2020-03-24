// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.rules;

import org.junit.runners.model.Statement;
import org.junit.AssumptionViolatedException;
import org.junit.runner.Description;
import java.util.concurrent.TimeUnit;

public abstract class Stopwatch implements TestRule
{
    private final Clock clock;
    private volatile long startNanos;
    private volatile long endNanos;
    
    public Stopwatch() {
        this(new Clock());
    }
    
    Stopwatch(final Clock clock) {
        this.clock = clock;
    }
    
    public long runtime(final TimeUnit unit) {
        return unit.convert(this.getNanos(), TimeUnit.NANOSECONDS);
    }
    
    protected void succeeded(final long nanos, final Description description) {
    }
    
    protected void failed(final long nanos, final Throwable e, final Description description) {
    }
    
    protected void skipped(final long nanos, final AssumptionViolatedException e, final Description description) {
    }
    
    protected void finished(final long nanos, final Description description) {
    }
    
    private long getNanos() {
        if (this.startNanos == 0L) {
            throw new IllegalStateException("Test has not started");
        }
        long currentEndNanos = this.endNanos;
        if (currentEndNanos == 0L) {
            currentEndNanos = this.clock.nanoTime();
        }
        return currentEndNanos - this.startNanos;
    }
    
    private void starting() {
        this.startNanos = this.clock.nanoTime();
        this.endNanos = 0L;
    }
    
    private void stopping() {
        this.endNanos = this.clock.nanoTime();
    }
    
    public final Statement apply(final Statement base, final Description description) {
        return new InternalWatcher().apply(base, description);
    }
    
    private class InternalWatcher extends TestWatcher
    {
        @Override
        protected void starting(final Description description) {
            Stopwatch.this.starting();
        }
        
        @Override
        protected void finished(final Description description) {
            Stopwatch.this.finished(Stopwatch.this.getNanos(), description);
        }
        
        @Override
        protected void succeeded(final Description description) {
            Stopwatch.this.stopping();
            Stopwatch.this.succeeded(Stopwatch.this.getNanos(), description);
        }
        
        @Override
        protected void failed(final Throwable e, final Description description) {
            Stopwatch.this.stopping();
            Stopwatch.this.failed(Stopwatch.this.getNanos(), e, description);
        }
        
        @Override
        protected void skipped(final AssumptionViolatedException e, final Description description) {
            Stopwatch.this.stopping();
            Stopwatch.this.skipped(Stopwatch.this.getNanos(), e, description);
        }
    }
    
    static class Clock
    {
        public long nanoTime() {
            return System.nanoTime();
        }
    }
}
