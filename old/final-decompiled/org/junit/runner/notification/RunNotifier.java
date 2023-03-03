// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runner.notification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import org.junit.runner.Result;
import org.junit.runner.Description;
import java.lang.annotation.Annotation;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class RunNotifier
{
    private final List<RunListener> listeners;
    private volatile boolean pleaseStop;
    
    public RunNotifier() {
        this.listeners = new CopyOnWriteArrayList<RunListener>();
        this.pleaseStop = false;
    }
    
    public void addListener(final RunListener listener) {
        if (listener == null) {
            throw new NullPointerException("Cannot add a null listener");
        }
        this.listeners.add(this.wrapIfNotThreadSafe(listener));
    }
    
    public void removeListener(final RunListener listener) {
        if (listener == null) {
            throw new NullPointerException("Cannot remove a null listener");
        }
        this.listeners.remove(this.wrapIfNotThreadSafe(listener));
    }
    
    RunListener wrapIfNotThreadSafe(final RunListener listener) {
        return listener.getClass().isAnnotationPresent(RunListener.ThreadSafe.class) ? listener : new SynchronizedRunListener(listener, this);
    }
    
    public void fireTestRunStarted(final Description description) {
        new SafeNotifier() {
            @Override
            protected void notifyListener(final RunListener each) throws Exception {
                each.testRunStarted(description);
            }
        }.run();
    }
    
    public void fireTestRunFinished(final Result result) {
        new SafeNotifier() {
            @Override
            protected void notifyListener(final RunListener each) throws Exception {
                each.testRunFinished(result);
            }
        }.run();
    }
    
    public void fireTestStarted(final Description description) throws StoppedByUserException {
        if (this.pleaseStop) {
            throw new StoppedByUserException();
        }
        new SafeNotifier() {
            @Override
            protected void notifyListener(final RunListener each) throws Exception {
                each.testStarted(description);
            }
        }.run();
    }
    
    public void fireTestFailure(final Failure failure) {
        this.fireTestFailures(this.listeners, Arrays.asList(failure));
    }
    
    private void fireTestFailures(final List<RunListener> listeners, final List<Failure> failures) {
        if (!failures.isEmpty()) {
            new SafeNotifier(listeners) {
                @Override
                protected void notifyListener(final RunListener listener) throws Exception {
                    for (final Failure each : failures) {
                        listener.testFailure(each);
                    }
                }
            }.run();
        }
    }
    
    public void fireTestAssumptionFailed(final Failure failure) {
        new SafeNotifier() {
            @Override
            protected void notifyListener(final RunListener each) throws Exception {
                each.testAssumptionFailure(failure);
            }
        }.run();
    }
    
    public void fireTestIgnored(final Description description) {
        new SafeNotifier() {
            @Override
            protected void notifyListener(final RunListener each) throws Exception {
                each.testIgnored(description);
            }
        }.run();
    }
    
    public void fireTestFinished(final Description description) {
        new SafeNotifier() {
            @Override
            protected void notifyListener(final RunListener each) throws Exception {
                each.testFinished(description);
            }
        }.run();
    }
    
    public void pleaseStop() {
        this.pleaseStop = true;
    }
    
    public void addFirstListener(final RunListener listener) {
        if (listener == null) {
            throw new NullPointerException("Cannot add a null listener");
        }
        this.listeners.add(0, this.wrapIfNotThreadSafe(listener));
    }
    
    private abstract class SafeNotifier
    {
        private final List<RunListener> currentListeners;
        
        SafeNotifier(final RunNotifier x0) {
            this(x0.listeners);
        }
        
        SafeNotifier(final List<RunListener> currentListeners) {
            this.currentListeners = currentListeners;
        }
        
        void run() {
            final int capacity = this.currentListeners.size();
            final ArrayList<RunListener> safeListeners = new ArrayList<RunListener>(capacity);
            final ArrayList<Failure> failures = new ArrayList<Failure>(capacity);
            for (final RunListener listener : this.currentListeners) {
                try {
                    this.notifyListener(listener);
                    safeListeners.add(listener);
                }
                catch (Exception e) {
                    failures.add(new Failure(Description.TEST_MECHANISM, e));
                }
            }
            RunNotifier.this.fireTestFailures(safeListeners, failures);
        }
        
        protected abstract void notifyListener(final RunListener p0) throws Exception;
    }
}
