// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners;

import java.util.Arrays;
import org.junit.validator.PublicClassValidator;
import org.junit.validator.AnnotationsValidator;
import java.util.Comparator;
import org.junit.runner.manipulation.Sorter;
import java.util.Collections;
import org.junit.runner.manipulation.NoTestsRemainException;
import java.util.ArrayList;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.ClassRule;
import org.junit.rules.TestRule;
import org.junit.rules.RunRules;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runners.model.Statement;
import org.junit.internal.runners.rules.RuleMemberValidator;
import org.junit.runners.model.FrameworkMethod;
import java.util.Iterator;
import org.junit.AfterClass;
import java.lang.annotation.Annotation;
import org.junit.BeforeClass;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.Description;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;
import java.util.Collection;
import org.junit.runners.model.TestClass;
import org.junit.validator.TestClassValidator;
import java.util.List;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.Runner;

public abstract class ParentRunner<T> extends Runner implements Filterable, Sortable
{
    private static final List<TestClassValidator> VALIDATORS;
    private final Object childrenLock;
    private final TestClass testClass;
    private volatile Collection<T> filteredChildren;
    private volatile RunnerScheduler scheduler;
    
    protected ParentRunner(final Class<?> testClass) throws InitializationError {
        this.childrenLock = new Object();
        this.filteredChildren = null;
        this.scheduler = new RunnerScheduler() {
            public void schedule(final Runnable childStatement) {
                childStatement.run();
            }
            
            public void finished() {
            }
        };
        this.testClass = this.createTestClass(testClass);
        this.validate();
    }
    
    protected TestClass createTestClass(final Class<?> testClass) {
        return new TestClass(testClass);
    }
    
    protected abstract List<T> getChildren();
    
    protected abstract Description describeChild(final T p0);
    
    protected abstract void runChild(final T p0, final RunNotifier p1);
    
    protected void collectInitializationErrors(final List<Throwable> errors) {
        this.validatePublicVoidNoArgMethods(BeforeClass.class, true, errors);
        this.validatePublicVoidNoArgMethods(AfterClass.class, true, errors);
        this.validateClassRules(errors);
        this.applyValidators(errors);
    }
    
    private void applyValidators(final List<Throwable> errors) {
        if (this.getTestClass().getJavaClass() != null) {
            for (final TestClassValidator each : ParentRunner.VALIDATORS) {
                errors.addAll(each.validateTestClass(this.getTestClass()));
            }
        }
    }
    
    protected void validatePublicVoidNoArgMethods(final Class<? extends Annotation> annotation, final boolean isStatic, final List<Throwable> errors) {
        final List<FrameworkMethod> methods = this.getTestClass().getAnnotatedMethods(annotation);
        for (final FrameworkMethod eachTestMethod : methods) {
            eachTestMethod.validatePublicVoidNoArg(isStatic, errors);
        }
    }
    
    private void validateClassRules(final List<Throwable> errors) {
        RuleMemberValidator.CLASS_RULE_VALIDATOR.validate(this.getTestClass(), errors);
        RuleMemberValidator.CLASS_RULE_METHOD_VALIDATOR.validate(this.getTestClass(), errors);
    }
    
    protected Statement classBlock(final RunNotifier notifier) {
        Statement statement = this.childrenInvoker(notifier);
        if (!this.areAllChildrenIgnored()) {
            statement = this.withBeforeClasses(statement);
            statement = this.withAfterClasses(statement);
            statement = this.withClassRules(statement);
        }
        return statement;
    }
    
    private boolean areAllChildrenIgnored() {
        for (final T child : this.getFilteredChildren()) {
            if (!this.isIgnored(child)) {
                return false;
            }
        }
        return true;
    }
    
    protected Statement withBeforeClasses(final Statement statement) {
        final List<FrameworkMethod> befores = this.testClass.getAnnotatedMethods(BeforeClass.class);
        return befores.isEmpty() ? statement : new RunBefores(statement, befores, null);
    }
    
    protected Statement withAfterClasses(final Statement statement) {
        final List<FrameworkMethod> afters = this.testClass.getAnnotatedMethods(AfterClass.class);
        return afters.isEmpty() ? statement : new RunAfters(statement, afters, null);
    }
    
    private Statement withClassRules(final Statement statement) {
        final List<TestRule> classRules = this.classRules();
        return classRules.isEmpty() ? statement : new RunRules(statement, classRules, this.getDescription());
    }
    
    protected List<TestRule> classRules() {
        final List<TestRule> result = this.testClass.getAnnotatedMethodValues(null, ClassRule.class, TestRule.class);
        result.addAll(this.testClass.getAnnotatedFieldValues(null, ClassRule.class, TestRule.class));
        return result;
    }
    
    protected Statement childrenInvoker(final RunNotifier notifier) {
        return new Statement() {
            @Override
            public void evaluate() {
                ParentRunner.this.runChildren(notifier);
            }
        };
    }
    
    protected boolean isIgnored(final T child) {
        return false;
    }
    
    private void runChildren(final RunNotifier notifier) {
        final RunnerScheduler currentScheduler = this.scheduler;
        try {
            for (final T each : this.getFilteredChildren()) {
                currentScheduler.schedule(new Runnable() {
                    public void run() {
                        ParentRunner.this.runChild(each, notifier);
                    }
                });
            }
        }
        finally {
            currentScheduler.finished();
        }
    }
    
    protected String getName() {
        return this.testClass.getName();
    }
    
    public final TestClass getTestClass() {
        return this.testClass;
    }
    
    protected final void runLeaf(final Statement statement, final Description description, final RunNotifier notifier) {
        final EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        eachNotifier.fireTestStarted();
        try {
            statement.evaluate();
        }
        catch (AssumptionViolatedException e) {
            eachNotifier.addFailedAssumption(e);
        }
        catch (Throwable e2) {
            eachNotifier.addFailure(e2);
        }
        finally {
            eachNotifier.fireTestFinished();
        }
    }
    
    protected Annotation[] getRunnerAnnotations() {
        return this.testClass.getAnnotations();
    }
    
    @Override
    public Description getDescription() {
        final Description description = Description.createSuiteDescription(this.getName(), this.getRunnerAnnotations());
        for (final T child : this.getFilteredChildren()) {
            description.addChild(this.describeChild(child));
        }
        return description;
    }
    
    @Override
    public void run(final RunNotifier notifier) {
        final EachTestNotifier testNotifier = new EachTestNotifier(notifier, this.getDescription());
        try {
            final Statement statement = this.classBlock(notifier);
            statement.evaluate();
        }
        catch (AssumptionViolatedException e) {
            testNotifier.addFailedAssumption(e);
        }
        catch (StoppedByUserException e2) {
            throw e2;
        }
        catch (Throwable e3) {
            testNotifier.addFailure(e3);
        }
    }
    
    public void filter(final Filter filter) throws NoTestsRemainException {
        synchronized (this.childrenLock) {
            final List<T> children = new ArrayList<T>(this.getFilteredChildren());
            final Iterator<T> iter = children.iterator();
            while (iter.hasNext()) {
                final T each = iter.next();
                if (this.shouldRun(filter, each)) {
                    try {
                        filter.apply(each);
                    }
                    catch (NoTestsRemainException e) {
                        iter.remove();
                    }
                }
                else {
                    iter.remove();
                }
            }
            this.filteredChildren = Collections.unmodifiableCollection((Collection<? extends T>)children);
            if (this.filteredChildren.isEmpty()) {
                throw new NoTestsRemainException();
            }
        }
    }
    
    public void sort(final Sorter sorter) {
        synchronized (this.childrenLock) {
            for (final T each : this.getFilteredChildren()) {
                sorter.apply(each);
            }
            final List<T> sortedChildren = new ArrayList<T>(this.getFilteredChildren());
            Collections.sort(sortedChildren, this.comparator(sorter));
            this.filteredChildren = Collections.unmodifiableCollection((Collection<? extends T>)sortedChildren);
        }
    }
    
    private void validate() throws InitializationError {
        final List<Throwable> errors = new ArrayList<Throwable>();
        this.collectInitializationErrors(errors);
        if (!errors.isEmpty()) {
            throw new InitializationError(errors);
        }
    }
    
    private Collection<T> getFilteredChildren() {
        if (this.filteredChildren == null) {
            synchronized (this.childrenLock) {
                if (this.filteredChildren == null) {
                    this.filteredChildren = Collections.unmodifiableCollection((Collection<? extends T>)this.getChildren());
                }
            }
        }
        return this.filteredChildren;
    }
    
    private boolean shouldRun(final Filter filter, final T each) {
        return filter.shouldRun(this.describeChild(each));
    }
    
    private Comparator<? super T> comparator(final Sorter sorter) {
        return new Comparator<T>() {
            public int compare(final T o1, final T o2) {
                return sorter.compare(ParentRunner.this.describeChild(o1), ParentRunner.this.describeChild(o2));
            }
        };
    }
    
    public void setScheduler(final RunnerScheduler scheduler) {
        this.scheduler = scheduler;
    }
    
    static {
        VALIDATORS = Arrays.asList(new AnnotationsValidator(), new PublicClassValidator());
    }
}
