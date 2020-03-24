// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners;

import org.junit.rules.RunRules;
import java.util.Collection;
import org.junit.Rule;
import java.util.Iterator;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import java.util.concurrent.TimeUnit;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.runners.model.Statement;
import org.junit.internal.runners.rules.RuleMemberValidator;
import org.junit.Before;
import org.junit.After;
import java.lang.annotation.Annotation;
import org.junit.Test;
import java.util.List;
import org.junit.Ignore;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runner.Description;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.runners.model.FrameworkMethod;

public class BlockJUnit4ClassRunner extends ParentRunner<FrameworkMethod>
{
    private final ConcurrentHashMap<FrameworkMethod, Description> methodDescriptions;
    
    public BlockJUnit4ClassRunner(final Class<?> klass) throws InitializationError {
        super(klass);
        this.methodDescriptions = new ConcurrentHashMap<FrameworkMethod, Description>();
    }
    
    @Override
    protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
        final Description description = this.describeChild(method);
        if (this.isIgnored(method)) {
            notifier.fireTestIgnored(description);
        }
        else {
            this.runLeaf(this.methodBlock(method), description, notifier);
        }
    }
    
    @Override
    protected boolean isIgnored(final FrameworkMethod child) {
        return child.getAnnotation(Ignore.class) != null;
    }
    
    @Override
    protected Description describeChild(final FrameworkMethod method) {
        Description description = this.methodDescriptions.get(method);
        if (description == null) {
            description = Description.createTestDescription(this.getTestClass().getJavaClass(), this.testName(method), method.getAnnotations());
            this.methodDescriptions.putIfAbsent(method, description);
        }
        return description;
    }
    
    @Override
    protected List<FrameworkMethod> getChildren() {
        return this.computeTestMethods();
    }
    
    protected List<FrameworkMethod> computeTestMethods() {
        return this.getTestClass().getAnnotatedMethods(Test.class);
    }
    
    @Override
    protected void collectInitializationErrors(final List<Throwable> errors) {
        super.collectInitializationErrors(errors);
        this.validateNoNonStaticInnerClass(errors);
        this.validateConstructor(errors);
        this.validateInstanceMethods(errors);
        this.validateFields(errors);
        this.validateMethods(errors);
    }
    
    protected void validateNoNonStaticInnerClass(final List<Throwable> errors) {
        if (this.getTestClass().isANonStaticInnerClass()) {
            final String gripe = "The inner class " + this.getTestClass().getName() + " is not static.";
            errors.add(new Exception(gripe));
        }
    }
    
    protected void validateConstructor(final List<Throwable> errors) {
        this.validateOnlyOneConstructor(errors);
        this.validateZeroArgConstructor(errors);
    }
    
    protected void validateOnlyOneConstructor(final List<Throwable> errors) {
        if (!this.hasOneConstructor()) {
            final String gripe = "Test class should have exactly one public constructor";
            errors.add(new Exception(gripe));
        }
    }
    
    protected void validateZeroArgConstructor(final List<Throwable> errors) {
        if (!this.getTestClass().isANonStaticInnerClass() && this.hasOneConstructor() && this.getTestClass().getOnlyConstructor().getParameterTypes().length != 0) {
            final String gripe = "Test class should have exactly one public zero-argument constructor";
            errors.add(new Exception(gripe));
        }
    }
    
    private boolean hasOneConstructor() {
        return this.getTestClass().getJavaClass().getConstructors().length == 1;
    }
    
    @Deprecated
    protected void validateInstanceMethods(final List<Throwable> errors) {
        this.validatePublicVoidNoArgMethods(After.class, false, errors);
        this.validatePublicVoidNoArgMethods(Before.class, false, errors);
        this.validateTestMethods(errors);
        if (this.computeTestMethods().size() == 0) {
            errors.add(new Exception("No runnable methods"));
        }
    }
    
    protected void validateFields(final List<Throwable> errors) {
        RuleMemberValidator.RULE_VALIDATOR.validate(this.getTestClass(), errors);
    }
    
    private void validateMethods(final List<Throwable> errors) {
        RuleMemberValidator.RULE_METHOD_VALIDATOR.validate(this.getTestClass(), errors);
    }
    
    protected void validateTestMethods(final List<Throwable> errors) {
        this.validatePublicVoidNoArgMethods(Test.class, false, errors);
    }
    
    protected Object createTest() throws Exception {
        return this.getTestClass().getOnlyConstructor().newInstance(new Object[0]);
    }
    
    protected String testName(final FrameworkMethod method) {
        return method.getName();
    }
    
    protected Statement methodBlock(final FrameworkMethod method) {
        Object test;
        try {
            test = new ReflectiveCallable() {
                @Override
                protected Object runReflectiveCall() throws Throwable {
                    return BlockJUnit4ClassRunner.this.createTest();
                }
            }.run();
        }
        catch (Throwable e) {
            return new Fail(e);
        }
        Statement statement = this.methodInvoker(method, test);
        statement = this.possiblyExpectingExceptions(method, test, statement);
        statement = this.withPotentialTimeout(method, test, statement);
        statement = this.withBefores(method, test, statement);
        statement = this.withAfters(method, test, statement);
        statement = this.withRules(method, test, statement);
        return statement;
    }
    
    protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
        return new InvokeMethod(method, test);
    }
    
    protected Statement possiblyExpectingExceptions(final FrameworkMethod method, final Object test, final Statement next) {
        final Test annotation = method.getAnnotation(Test.class);
        return this.expectsException(annotation) ? new ExpectException(next, this.getExpectedException(annotation)) : next;
    }
    
    @Deprecated
    protected Statement withPotentialTimeout(final FrameworkMethod method, final Object test, final Statement next) {
        final long timeout = this.getTimeout(method.getAnnotation(Test.class));
        if (timeout <= 0L) {
            return next;
        }
        return FailOnTimeout.builder().withTimeout(timeout, TimeUnit.MILLISECONDS).build(next);
    }
    
    protected Statement withBefores(final FrameworkMethod method, final Object target, final Statement statement) {
        final List<FrameworkMethod> befores = this.getTestClass().getAnnotatedMethods(Before.class);
        return befores.isEmpty() ? statement : new RunBefores(statement, befores, target);
    }
    
    protected Statement withAfters(final FrameworkMethod method, final Object target, final Statement statement) {
        final List<FrameworkMethod> afters = this.getTestClass().getAnnotatedMethods(After.class);
        return afters.isEmpty() ? statement : new RunAfters(statement, afters, target);
    }
    
    private Statement withRules(final FrameworkMethod method, final Object target, final Statement statement) {
        final List<TestRule> testRules = this.getTestRules(target);
        Statement result = statement;
        result = this.withMethodRules(method, testRules, target, result);
        result = this.withTestRules(method, testRules, result);
        return result;
    }
    
    private Statement withMethodRules(final FrameworkMethod method, final List<TestRule> testRules, final Object target, Statement result) {
        for (final MethodRule each : this.getMethodRules(target)) {
            if (!testRules.contains(each)) {
                result = each.apply(result, method, target);
            }
        }
        return result;
    }
    
    private List<MethodRule> getMethodRules(final Object target) {
        return this.rules(target);
    }
    
    protected List<MethodRule> rules(final Object target) {
        final List<MethodRule> rules = this.getTestClass().getAnnotatedMethodValues(target, Rule.class, MethodRule.class);
        rules.addAll(this.getTestClass().getAnnotatedFieldValues(target, Rule.class, MethodRule.class));
        return rules;
    }
    
    private Statement withTestRules(final FrameworkMethod method, final List<TestRule> testRules, final Statement statement) {
        return testRules.isEmpty() ? statement : new RunRules(statement, testRules, this.describeChild(method));
    }
    
    protected List<TestRule> getTestRules(final Object target) {
        final List<TestRule> result = this.getTestClass().getAnnotatedMethodValues(target, Rule.class, TestRule.class);
        result.addAll(this.getTestClass().getAnnotatedFieldValues(target, Rule.class, TestRule.class));
        return result;
    }
    
    private Class<? extends Throwable> getExpectedException(final Test annotation) {
        if (annotation == null || annotation.expected() == Test.None.class) {
            return null;
        }
        return annotation.expected();
    }
    
    private boolean expectsException(final Test annotation) {
        return this.getExpectedException(annotation) != null;
    }
    
    private long getTimeout(final Test annotation) {
        if (annotation == null) {
            return 0L;
        }
        return annotation.timeout();
    }
}
