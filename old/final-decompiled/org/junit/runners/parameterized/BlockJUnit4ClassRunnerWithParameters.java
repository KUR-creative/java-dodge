// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners.parameterized;

import java.lang.annotation.Annotation;
import org.junit.runners.model.Statement;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import org.junit.runners.Parameterized;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.InitializationError;
import org.junit.runners.BlockJUnit4ClassRunner;

public class BlockJUnit4ClassRunnerWithParameters extends BlockJUnit4ClassRunner
{
    private final Object[] parameters;
    private final String name;
    
    public BlockJUnit4ClassRunnerWithParameters(final TestWithParameters test) throws InitializationError {
        super(test.getTestClass().getJavaClass());
        this.parameters = test.getParameters().toArray(new Object[test.getParameters().size()]);
        this.name = test.getName();
    }
    
    public Object createTest() throws Exception {
        if (this.fieldsAreAnnotated()) {
            return this.createTestUsingFieldInjection();
        }
        return this.createTestUsingConstructorInjection();
    }
    
    private Object createTestUsingConstructorInjection() throws Exception {
        return this.getTestClass().getOnlyConstructor().newInstance(this.parameters);
    }
    
    private Object createTestUsingFieldInjection() throws Exception {
        final List<FrameworkField> annotatedFieldsByParameter = this.getAnnotatedFieldsByParameter();
        if (annotatedFieldsByParameter.size() != this.parameters.length) {
            throw new Exception("Wrong number of parameters and @Parameter fields. @Parameter fields counted: " + annotatedFieldsByParameter.size() + ", available parameters: " + this.parameters.length + ".");
        }
        final Object testClassInstance = this.getTestClass().getJavaClass().newInstance();
        for (final FrameworkField each : annotatedFieldsByParameter) {
            final Field field = each.getField();
            final Parameterized.Parameter annotation = field.getAnnotation(Parameterized.Parameter.class);
            final int index = annotation.value();
            try {
                field.set(testClassInstance, this.parameters[index]);
            }
            catch (IllegalArgumentException iare) {
                throw new Exception(this.getTestClass().getName() + ": Trying to set " + field.getName() + " with the value " + this.parameters[index] + " that is not the right type (" + this.parameters[index].getClass().getSimpleName() + " instead of " + field.getType().getSimpleName() + ").", iare);
            }
        }
        return testClassInstance;
    }
    
    @Override
    protected String getName() {
        return this.name;
    }
    
    @Override
    protected String testName(final FrameworkMethod method) {
        return method.getName() + this.getName();
    }
    
    @Override
    protected void validateConstructor(final List<Throwable> errors) {
        this.validateOnlyOneConstructor(errors);
        if (this.fieldsAreAnnotated()) {
            this.validateZeroArgConstructor(errors);
        }
    }
    
    @Override
    protected void validateFields(final List<Throwable> errors) {
        super.validateFields(errors);
        if (this.fieldsAreAnnotated()) {
            final List<FrameworkField> annotatedFieldsByParameter = this.getAnnotatedFieldsByParameter();
            final int[] usedIndices = new int[annotatedFieldsByParameter.size()];
            for (final FrameworkField each : annotatedFieldsByParameter) {
                final int index = each.getField().getAnnotation(Parameterized.Parameter.class).value();
                if (index < 0 || index > annotatedFieldsByParameter.size() - 1) {
                    errors.add(new Exception("Invalid @Parameter value: " + index + ". @Parameter fields counted: " + annotatedFieldsByParameter.size() + ". Please use an index between 0 and " + (annotatedFieldsByParameter.size() - 1) + "."));
                }
                else {
                    final int[] array = usedIndices;
                    final int n = index;
                    ++array[n];
                }
            }
            for (int index2 = 0; index2 < usedIndices.length; ++index2) {
                final int numberOfUse = usedIndices[index2];
                if (numberOfUse == 0) {
                    errors.add(new Exception("@Parameter(" + index2 + ") is never used."));
                }
                else if (numberOfUse > 1) {
                    errors.add(new Exception("@Parameter(" + index2 + ") is used more than once (" + numberOfUse + ")."));
                }
            }
        }
    }
    
    @Override
    protected Statement classBlock(final RunNotifier notifier) {
        return this.childrenInvoker(notifier);
    }
    
    @Override
    protected Annotation[] getRunnerAnnotations() {
        return new Annotation[0];
    }
    
    private List<FrameworkField> getAnnotatedFieldsByParameter() {
        return this.getTestClass().getAnnotatedFields(Parameterized.Parameter.class);
    }
    
    private boolean fieldsAreAnnotated() {
        return !this.getAnnotatedFieldsByParameter().isEmpty();
    }
}
