// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners;

import java.lang.annotation.Inherited;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.Description;
import java.util.Collections;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.InitializationError;
import java.util.List;
import org.junit.runner.Runner;

public class Suite extends ParentRunner<Runner>
{
    private final List<Runner> runners;
    
    public static Runner emptySuite() {
        try {
            return new Suite((Class<?>)null, new Class[0]);
        }
        catch (InitializationError e) {
            throw new RuntimeException("This shouldn't be possible");
        }
    }
    
    private static Class<?>[] getAnnotatedClasses(final Class<?> klass) throws InitializationError {
        final SuiteClasses annotation = klass.getAnnotation(SuiteClasses.class);
        if (annotation == null) {
            throw new InitializationError(String.format("class '%s' must have a SuiteClasses annotation", klass.getName()));
        }
        return annotation.value();
    }
    
    public Suite(final Class<?> klass, final RunnerBuilder builder) throws InitializationError {
        this(builder, klass, getAnnotatedClasses(klass));
    }
    
    public Suite(final RunnerBuilder builder, final Class<?>[] classes) throws InitializationError {
        this(null, builder.runners(null, classes));
    }
    
    protected Suite(final Class<?> klass, final Class<?>[] suiteClasses) throws InitializationError {
        this(new AllDefaultPossibilitiesBuilder(true), klass, suiteClasses);
    }
    
    protected Suite(final RunnerBuilder builder, final Class<?> klass, final Class<?>[] suiteClasses) throws InitializationError {
        this(klass, builder.runners(klass, suiteClasses));
    }
    
    protected Suite(final Class<?> klass, final List<Runner> runners) throws InitializationError {
        super(klass);
        this.runners = Collections.unmodifiableList((List<? extends Runner>)runners);
    }
    
    @Override
    protected List<Runner> getChildren() {
        return this.runners;
    }
    
    @Override
    protected Description describeChild(final Runner child) {
        return child.getDescription();
    }
    
    @Override
    protected void runChild(final Runner runner, final RunNotifier notifier) {
        runner.run(notifier);
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    @Inherited
    public @interface SuiteClasses {
        Class<?>[] value();
    }
}
