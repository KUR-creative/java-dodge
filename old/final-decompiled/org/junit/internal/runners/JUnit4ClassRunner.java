// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners;

import java.util.Collections;
import java.util.Comparator;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.notification.Failure;
import java.lang.reflect.InvocationTargetException;
import java.lang.annotation.Annotation;
import org.junit.runner.Description;
import java.util.Iterator;
import org.junit.runner.notification.RunNotifier;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.Runner;

@Deprecated
public class JUnit4ClassRunner extends Runner implements Filterable, Sortable
{
    private final List<Method> testMethods;
    private TestClass testClass;
    
    public JUnit4ClassRunner(final Class<?> klass) throws InitializationError {
        this.testClass = new TestClass(klass);
        this.testMethods = this.getTestMethods();
        this.validate();
    }
    
    protected List<Method> getTestMethods() {
        return this.testClass.getTestMethods();
    }
    
    protected void validate() throws InitializationError {
        final MethodValidator methodValidator = new MethodValidator(this.testClass);
        methodValidator.validateMethodsForDefaultRunner();
        methodValidator.assertValid();
    }
    
    @Override
    public void run(final RunNotifier notifier) {
        new ClassRoadie(notifier, this.testClass, this.getDescription(), new Runnable() {
            public void run() {
                JUnit4ClassRunner.this.runMethods(notifier);
            }
        }).runProtected();
    }
    
    protected void runMethods(final RunNotifier notifier) {
        for (final Method method : this.testMethods) {
            this.invokeTestMethod(method, notifier);
        }
    }
    
    @Override
    public Description getDescription() {
        final Description spec = Description.createSuiteDescription(this.getName(), this.classAnnotations());
        final List<Method> testMethods = this.testMethods;
        for (final Method method : testMethods) {
            spec.addChild(this.methodDescription(method));
        }
        return spec;
    }
    
    protected Annotation[] classAnnotations() {
        return this.testClass.getJavaClass().getAnnotations();
    }
    
    protected String getName() {
        return this.getTestClass().getName();
    }
    
    protected Object createTest() throws Exception {
        return this.getTestClass().getConstructor().newInstance(new Object[0]);
    }
    
    protected void invokeTestMethod(final Method method, final RunNotifier notifier) {
        final Description description = this.methodDescription(method);
        Object test;
        try {
            test = this.createTest();
        }
        catch (InvocationTargetException e) {
            this.testAborted(notifier, description, e.getCause());
            return;
        }
        catch (Exception e2) {
            this.testAborted(notifier, description, e2);
            return;
        }
        final TestMethod testMethod = this.wrapMethod(method);
        new MethodRoadie(test, testMethod, notifier, description).run();
    }
    
    private void testAborted(final RunNotifier notifier, final Description description, final Throwable e) {
        notifier.fireTestStarted(description);
        notifier.fireTestFailure(new Failure(description, e));
        notifier.fireTestFinished(description);
    }
    
    protected TestMethod wrapMethod(final Method method) {
        return new TestMethod(method, this.testClass);
    }
    
    protected String testName(final Method method) {
        return method.getName();
    }
    
    protected Description methodDescription(final Method method) {
        return Description.createTestDescription(this.getTestClass().getJavaClass(), this.testName(method), this.testAnnotations(method));
    }
    
    protected Annotation[] testAnnotations(final Method method) {
        return method.getAnnotations();
    }
    
    public void filter(final Filter filter) throws NoTestsRemainException {
        final Iterator<Method> iter = this.testMethods.iterator();
        while (iter.hasNext()) {
            final Method method = iter.next();
            if (!filter.shouldRun(this.methodDescription(method))) {
                iter.remove();
            }
        }
        if (this.testMethods.isEmpty()) {
            throw new NoTestsRemainException();
        }
    }
    
    public void sort(final Sorter sorter) {
        Collections.sort(this.testMethods, new Comparator<Method>() {
            public int compare(final Method o1, final Method o2) {
                return sorter.compare(JUnit4ClassRunner.this.methodDescription(o1), JUnit4ClassRunner.this.methodDescription(o2));
            }
        });
    }
    
    protected TestClass getTestClass() {
        return this.testClass;
    }
}
