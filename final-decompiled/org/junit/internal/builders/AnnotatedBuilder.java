// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.builders;

import org.junit.runners.model.InitializationError;
import java.lang.reflect.Modifier;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

public class AnnotatedBuilder extends RunnerBuilder
{
    private static final String CONSTRUCTOR_ERROR_FORMAT = "Custom runner class %s should have a public constructor with signature %s(Class testClass)";
    private final RunnerBuilder suiteBuilder;
    
    public AnnotatedBuilder(final RunnerBuilder suiteBuilder) {
        this.suiteBuilder = suiteBuilder;
    }
    
    @Override
    public Runner runnerForClass(final Class<?> testClass) throws Exception {
        for (Class<?> currentTestClass = testClass; currentTestClass != null; currentTestClass = this.getEnclosingClassForNonStaticMemberClass(currentTestClass)) {
            final RunWith annotation = currentTestClass.getAnnotation(RunWith.class);
            if (annotation != null) {
                return this.buildRunner(annotation.value(), testClass);
            }
        }
        return null;
    }
    
    private Class<?> getEnclosingClassForNonStaticMemberClass(final Class<?> currentTestClass) {
        if (currentTestClass.isMemberClass() && !Modifier.isStatic(currentTestClass.getModifiers())) {
            return currentTestClass.getEnclosingClass();
        }
        return null;
    }
    
    public Runner buildRunner(final Class<? extends Runner> runnerClass, final Class<?> testClass) throws Exception {
        try {
            return (Runner)runnerClass.getConstructor(Class.class).newInstance(testClass);
        }
        catch (NoSuchMethodException e) {
            try {
                return (Runner)runnerClass.getConstructor(Class.class, RunnerBuilder.class).newInstance(testClass, this.suiteBuilder);
            }
            catch (NoSuchMethodException e2) {
                final String simpleName = runnerClass.getSimpleName();
                throw new InitializationError(String.format("Custom runner class %s should have a public constructor with signature %s(Class testClass)", simpleName, simpleName));
            }
        }
    }
}
