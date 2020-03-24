// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Inherited;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParametersFactory;
import org.junit.runners.model.TestClass;
import java.text.MessageFormat;
import org.junit.runners.model.InitializationError;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.annotation.Annotation;
import org.junit.runners.model.FrameworkMethod;
import java.util.Arrays;
import org.junit.runners.parameterized.TestWithParameters;
import java.util.Collections;
import org.junit.runner.Runner;
import java.util.List;
import org.junit.runners.parameterized.ParametersRunnerFactory;

public class Parameterized extends Suite
{
    private static final ParametersRunnerFactory DEFAULT_FACTORY;
    private static final List<Runner> NO_RUNNERS;
    private final List<Runner> runners;
    
    public Parameterized(final Class<?> klass) throws Throwable {
        super(klass, Parameterized.NO_RUNNERS);
        final ParametersRunnerFactory runnerFactory = this.getParametersRunnerFactory(klass);
        final Parameters parameters = this.getParametersMethod().getAnnotation(Parameters.class);
        this.runners = Collections.unmodifiableList((List<? extends Runner>)this.createRunnersForParameters(this.allParameters(), parameters.name(), runnerFactory));
    }
    
    private ParametersRunnerFactory getParametersRunnerFactory(final Class<?> klass) throws InstantiationException, IllegalAccessException {
        final UseParametersRunnerFactory annotation = klass.getAnnotation(UseParametersRunnerFactory.class);
        if (annotation == null) {
            return Parameterized.DEFAULT_FACTORY;
        }
        final Class<? extends ParametersRunnerFactory> factoryClass = annotation.value();
        return (ParametersRunnerFactory)factoryClass.newInstance();
    }
    
    @Override
    protected List<Runner> getChildren() {
        return this.runners;
    }
    
    private TestWithParameters createTestWithNotNormalizedParameters(final String pattern, final int index, final Object parametersOrSingleParameter) {
        final Object[] parameters = (Object[])((parametersOrSingleParameter instanceof Object[]) ? parametersOrSingleParameter : new Object[] { parametersOrSingleParameter });
        return createTestWithParameters(this.getTestClass(), pattern, index, parameters);
    }
    
    private Iterable<Object> allParameters() throws Throwable {
        final Object parameters = this.getParametersMethod().invokeExplosively(null, new Object[0]);
        if (parameters instanceof Iterable) {
            return (Iterable<Object>)parameters;
        }
        if (parameters instanceof Object[]) {
            return Arrays.asList((Object[])parameters);
        }
        throw this.parametersMethodReturnedWrongType();
    }
    
    private FrameworkMethod getParametersMethod() throws Exception {
        final List<FrameworkMethod> methods = this.getTestClass().getAnnotatedMethods(Parameters.class);
        for (final FrameworkMethod each : methods) {
            if (each.isStatic() && each.isPublic()) {
                return each;
            }
        }
        throw new Exception("No public static parameters method on class " + this.getTestClass().getName());
    }
    
    private List<Runner> createRunnersForParameters(final Iterable<Object> allParameters, final String namePattern, final ParametersRunnerFactory runnerFactory) throws InitializationError, Exception {
        try {
            final List<TestWithParameters> tests = this.createTestsForParameters(allParameters, namePattern);
            final List<Runner> runners = new ArrayList<Runner>();
            for (final TestWithParameters test : tests) {
                runners.add(runnerFactory.createRunnerForTestWithParameters(test));
            }
            return runners;
        }
        catch (ClassCastException e) {
            throw this.parametersMethodReturnedWrongType();
        }
    }
    
    private List<TestWithParameters> createTestsForParameters(final Iterable<Object> allParameters, final String namePattern) throws Exception {
        int i = 0;
        final List<TestWithParameters> children = new ArrayList<TestWithParameters>();
        for (final Object parametersOfSingleTest : allParameters) {
            children.add(this.createTestWithNotNormalizedParameters(namePattern, i++, parametersOfSingleTest));
        }
        return children;
    }
    
    private Exception parametersMethodReturnedWrongType() throws Exception {
        final String className = this.getTestClass().getName();
        final String methodName = this.getParametersMethod().getName();
        final String message = MessageFormat.format("{0}.{1}() must return an Iterable of arrays.", className, methodName);
        return new Exception(message);
    }
    
    private static TestWithParameters createTestWithParameters(final TestClass testClass, final String pattern, final int index, final Object[] parameters) {
        final String finalPattern = pattern.replaceAll("\\{index\\}", Integer.toString(index));
        final String name = MessageFormat.format(finalPattern, parameters);
        return new TestWithParameters("[" + name + "]", testClass, Arrays.asList(parameters));
    }
    
    static {
        DEFAULT_FACTORY = new BlockJUnit4ClassRunnerWithParametersFactory();
        NO_RUNNERS = Collections.emptyList();
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Target({ ElementType.TYPE })
    public @interface UseParametersRunnerFactory {
        Class<? extends ParametersRunnerFactory> value() default BlockJUnit4ClassRunnerWithParametersFactory.class;
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Parameter {
        int value() default 0;
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD })
    public @interface Parameters {
        String name() default "{index}";
    }
}
