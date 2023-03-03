// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners;

import org.junit.runner.notification.Failure;
import java.util.Iterator;
import java.util.List;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import org.junit.internal.AssumptionViolatedException;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;
import org.junit.runners.model.TestTimedOutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

@Deprecated
public class MethodRoadie
{
    private final Object test;
    private final RunNotifier notifier;
    private final Description description;
    private TestMethod testMethod;
    
    public MethodRoadie(final Object test, final TestMethod method, final RunNotifier notifier, final Description description) {
        this.test = test;
        this.notifier = notifier;
        this.description = description;
        this.testMethod = method;
    }
    
    public void run() {
        if (this.testMethod.isIgnored()) {
            this.notifier.fireTestIgnored(this.description);
            return;
        }
        this.notifier.fireTestStarted(this.description);
        try {
            final long timeout = this.testMethod.getTimeout();
            if (timeout > 0L) {
                this.runWithTimeout(timeout);
            }
            else {
                this.runTest();
            }
        }
        finally {
            this.notifier.fireTestFinished(this.description);
        }
    }
    
    private void runWithTimeout(final long timeout) {
        this.runBeforesThenTestThenAfters(new Runnable() {
            public void run() {
                final ExecutorService service = Executors.newSingleThreadExecutor();
                final Callable<Object> callable = new Callable<Object>() {
                    public Object call() throws Exception {
                        MethodRoadie.this.runTestMethod();
                        return null;
                    }
                };
                final Future<Object> result = service.submit(callable);
                service.shutdown();
                try {
                    final boolean terminated = service.awaitTermination(timeout, TimeUnit.MILLISECONDS);
                    if (!terminated) {
                        service.shutdownNow();
                    }
                    result.get(0L, TimeUnit.MILLISECONDS);
                }
                catch (TimeoutException e2) {
                    MethodRoadie.this.addFailure(new TestTimedOutException(timeout, TimeUnit.MILLISECONDS));
                }
                catch (Exception e) {
                    MethodRoadie.this.addFailure(e);
                }
            }
        });
    }
    
    public void runTest() {
        this.runBeforesThenTestThenAfters(new Runnable() {
            public void run() {
                MethodRoadie.this.runTestMethod();
            }
        });
    }
    
    public void runBeforesThenTestThenAfters(final Runnable test) {
        try {
            this.runBefores();
            test.run();
        }
        catch (FailedBefore e) {}
        catch (Exception e2) {
            throw new RuntimeException("test should never throw an exception to this level");
        }
        finally {
            this.runAfters();
        }
    }
    
    protected void runTestMethod() {
        try {
            this.testMethod.invoke(this.test);
            if (this.testMethod.expectsException()) {
                this.addFailure(new AssertionError((Object)("Expected exception: " + this.testMethod.getExpectedException().getName())));
            }
        }
        catch (InvocationTargetException e) {
            final Throwable actual = e.getTargetException();
            if (actual instanceof AssumptionViolatedException) {
                return;
            }
            if (!this.testMethod.expectsException()) {
                this.addFailure(actual);
            }
            else if (this.testMethod.isUnexpected(actual)) {
                final String message = "Unexpected exception, expected<" + this.testMethod.getExpectedException().getName() + "> but was<" + actual.getClass().getName() + ">";
                this.addFailure(new Exception(message, actual));
            }
        }
        catch (Throwable e2) {
            this.addFailure(e2);
        }
    }
    
    private void runBefores() throws FailedBefore {
        try {
            try {
                final List<Method> befores = this.testMethod.getBefores();
                for (final Method before : befores) {
                    before.invoke(this.test, new Object[0]);
                }
            }
            catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
        catch (AssumptionViolatedException e3) {
            throw new FailedBefore();
        }
        catch (Throwable e2) {
            this.addFailure(e2);
            throw new FailedBefore();
        }
    }
    
    private void runAfters() {
        final List<Method> afters = this.testMethod.getAfters();
        for (final Method after : afters) {
            try {
                after.invoke(this.test, new Object[0]);
            }
            catch (InvocationTargetException e) {
                this.addFailure(e.getTargetException());
            }
            catch (Throwable e2) {
                this.addFailure(e2);
            }
        }
    }
    
    protected void addFailure(final Throwable e) {
        this.notifier.fireTestFailure(new Failure(this.description, e));
    }
}
