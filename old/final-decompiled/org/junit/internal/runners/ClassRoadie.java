// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners;

import java.util.Iterator;
import java.util.List;
import org.junit.internal.AssumptionViolatedException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.runner.notification.Failure;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

@Deprecated
public class ClassRoadie
{
    private RunNotifier notifier;
    private TestClass testClass;
    private Description description;
    private final Runnable runnable;
    
    public ClassRoadie(final RunNotifier notifier, final TestClass testClass, final Description description, final Runnable runnable) {
        this.notifier = notifier;
        this.testClass = testClass;
        this.description = description;
        this.runnable = runnable;
    }
    
    protected void runUnprotected() {
        this.runnable.run();
    }
    
    protected void addFailure(final Throwable targetException) {
        this.notifier.fireTestFailure(new Failure(this.description, targetException));
    }
    
    public void runProtected() {
        try {
            this.runBefores();
            this.runUnprotected();
        }
        catch (FailedBefore e) {}
        finally {
            this.runAfters();
        }
    }
    
    private void runBefores() throws FailedBefore {
        try {
            try {
                final List<Method> befores = this.testClass.getBefores();
                for (final Method before : befores) {
                    before.invoke(null, new Object[0]);
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
        final List<Method> afters = this.testClass.getAfters();
        for (final Method after : afters) {
            try {
                after.invoke(null, new Object[0]);
            }
            catch (InvocationTargetException e) {
                this.addFailure(e.getTargetException());
            }
            catch (Throwable e2) {
                this.addFailure(e2);
            }
        }
    }
}
