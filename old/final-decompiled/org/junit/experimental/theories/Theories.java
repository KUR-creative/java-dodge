// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.theories;

import org.junit.experimental.theories.internal.ParameterizedAssertionError;
import org.junit.Assume;
import org.junit.Assert;
import org.junit.experimental.theories.internal.Assignments;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.model.Statement;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.ArrayList;
import java.lang.reflect.Constructor;
import org.junit.runners.model.TestClass;
import java.util.Iterator;
import org.junit.runners.model.FrameworkMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import org.junit.runners.model.InitializationError;
import org.junit.runners.BlockJUnit4ClassRunner;

public class Theories extends BlockJUnit4ClassRunner
{
    public Theories(final Class<?> klass) throws InitializationError {
        super(klass);
    }
    
    @Override
    protected void collectInitializationErrors(final List<Throwable> errors) {
        super.collectInitializationErrors(errors);
        this.validateDataPointFields(errors);
        this.validateDataPointMethods(errors);
    }
    
    private void validateDataPointFields(final List<Throwable> errors) {
        final Field[] arr$;
        final Field[] fields = arr$ = this.getTestClass().getJavaClass().getDeclaredFields();
        for (final Field field : arr$) {
            if (field.getAnnotation(DataPoint.class) != null || field.getAnnotation(DataPoints.class) != null) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    errors.add(new Error("DataPoint field " + field.getName() + " must be static"));
                }
                if (!Modifier.isPublic(field.getModifiers())) {
                    errors.add(new Error("DataPoint field " + field.getName() + " must be public"));
                }
            }
        }
    }
    
    private void validateDataPointMethods(final List<Throwable> errors) {
        final Method[] arr$;
        final Method[] methods = arr$ = this.getTestClass().getJavaClass().getDeclaredMethods();
        for (final Method method : arr$) {
            if (method.getAnnotation(DataPoint.class) != null || method.getAnnotation(DataPoints.class) != null) {
                if (!Modifier.isStatic(method.getModifiers())) {
                    errors.add(new Error("DataPoint method " + method.getName() + " must be static"));
                }
                if (!Modifier.isPublic(method.getModifiers())) {
                    errors.add(new Error("DataPoint method " + method.getName() + " must be public"));
                }
            }
        }
    }
    
    @Override
    protected void validateConstructor(final List<Throwable> errors) {
        this.validateOnlyOneConstructor(errors);
    }
    
    @Override
    protected void validateTestMethods(final List<Throwable> errors) {
        for (final FrameworkMethod each : this.computeTestMethods()) {
            if (each.getAnnotation(Theory.class) != null) {
                each.validatePublicVoid(false, errors);
                each.validateNoTypeParametersOnArgs(errors);
            }
            else {
                each.validatePublicVoidNoArg(false, errors);
            }
            for (final ParameterSignature signature : ParameterSignature.signatures(each.getMethod())) {
                final ParametersSuppliedBy annotation = signature.findDeepAnnotation(ParametersSuppliedBy.class);
                if (annotation != null) {
                    this.validateParameterSupplier(annotation.value(), errors);
                }
            }
        }
    }
    
    private void validateParameterSupplier(final Class<? extends ParameterSupplier> supplierClass, final List<Throwable> errors) {
        final Constructor<?>[] constructors = supplierClass.getConstructors();
        if (constructors.length != 1) {
            errors.add(new Error("ParameterSupplier " + supplierClass.getName() + " must have only one constructor (either empty or taking only a TestClass)"));
        }
        else {
            final Class<?>[] paramTypes = constructors[0].getParameterTypes();
            if (paramTypes.length != 0 && !paramTypes[0].equals(TestClass.class)) {
                errors.add(new Error("ParameterSupplier " + supplierClass.getName() + " constructor must take either nothing or a single TestClass instance"));
            }
        }
    }
    
    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        final List<FrameworkMethod> testMethods = new ArrayList<FrameworkMethod>(super.computeTestMethods());
        final List<FrameworkMethod> theoryMethods = this.getTestClass().getAnnotatedMethods(Theory.class);
        testMethods.removeAll(theoryMethods);
        testMethods.addAll(theoryMethods);
        return testMethods;
    }
    
    public Statement methodBlock(final FrameworkMethod method) {
        return new TheoryAnchor(method, this.getTestClass());
    }
    
    public static class TheoryAnchor extends Statement
    {
        private int successes;
        private final FrameworkMethod testMethod;
        private final TestClass testClass;
        private List<AssumptionViolatedException> fInvalidParameters;
        
        public TheoryAnchor(final FrameworkMethod testMethod, final TestClass testClass) {
            this.successes = 0;
            this.fInvalidParameters = new ArrayList<AssumptionViolatedException>();
            this.testMethod = testMethod;
            this.testClass = testClass;
        }
        
        private TestClass getTestClass() {
            return this.testClass;
        }
        
        @Override
        public void evaluate() throws Throwable {
            this.runWithAssignment(Assignments.allUnassigned(this.testMethod.getMethod(), this.getTestClass()));
            final boolean hasTheoryAnnotation = this.testMethod.getAnnotation(Theory.class) != null;
            if (this.successes == 0 && hasTheoryAnnotation) {
                Assert.fail("Never found parameters that satisfied method assumptions.  Violated assumptions: " + this.fInvalidParameters);
            }
        }
        
        protected void runWithAssignment(final Assignments parameterAssignment) throws Throwable {
            if (!parameterAssignment.isComplete()) {
                this.runWithIncompleteAssignment(parameterAssignment);
            }
            else {
                this.runWithCompleteAssignment(parameterAssignment);
            }
        }
        
        protected void runWithIncompleteAssignment(final Assignments incomplete) throws Throwable {
            for (final PotentialAssignment source : incomplete.potentialsForNextUnassigned()) {
                this.runWithAssignment(incomplete.assignNext(source));
            }
        }
        
        protected void runWithCompleteAssignment(final Assignments complete) throws Throwable {
            new BlockJUnit4ClassRunner(this.getTestClass().getJavaClass()) {
                @Override
                protected void collectInitializationErrors(final List<Throwable> errors) {
                }
                
                public Statement methodBlock(final FrameworkMethod method) {
                    final Statement statement = super.methodBlock(method);
                    return new Statement() {
                        @Override
                        public void evaluate() throws Throwable {
                            try {
                                statement.evaluate();
                                TheoryAnchor.this.handleDataPointSuccess();
                            }
                            catch (AssumptionViolatedException e) {
                                TheoryAnchor.this.handleAssumptionViolation(e);
                            }
                            catch (Throwable e2) {
                                TheoryAnchor.this.reportParameterizedError(e2, complete.getArgumentStrings(TheoryAnchor.this.nullsOk()));
                            }
                        }
                    };
                }
                
                @Override
                protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
                    return TheoryAnchor.this.methodCompletesWithParameters(method, complete, test);
                }
                
                public Object createTest() throws Exception {
                    final Object[] params = complete.getConstructorArguments();
                    if (!TheoryAnchor.this.nullsOk()) {
                        Assume.assumeNotNull(params);
                    }
                    return this.getTestClass().getOnlyConstructor().newInstance(params);
                }
            }.methodBlock(this.testMethod).evaluate();
        }
        
        private Statement methodCompletesWithParameters(final FrameworkMethod method, final Assignments complete, final Object freshInstance) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    final Object[] values = complete.getMethodArguments();
                    if (!TheoryAnchor.this.nullsOk()) {
                        Assume.assumeNotNull(values);
                    }
                    method.invokeExplosively(freshInstance, values);
                }
            };
        }
        
        protected void handleAssumptionViolation(final AssumptionViolatedException e) {
            this.fInvalidParameters.add(e);
        }
        
        protected void reportParameterizedError(final Throwable e, final Object... params) throws Throwable {
            if (params.length == 0) {
                throw e;
            }
            throw new ParameterizedAssertionError(e, this.testMethod.getName(), params);
        }
        
        private boolean nullsOk() {
            final Theory annotation = this.testMethod.getMethod().getAnnotation(Theory.class);
            return annotation != null && annotation.nullsAccepted();
        }
        
        protected void handleDataPointSuccess() {
            ++this.successes;
        }
    }
}
