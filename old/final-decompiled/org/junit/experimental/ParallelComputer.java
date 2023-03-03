// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental;

import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import org.junit.runners.model.RunnerScheduler;
import org.junit.runners.ParentRunner;
import org.junit.runner.Runner;
import org.junit.runner.Computer;

public class ParallelComputer extends Computer
{
    private final boolean classes;
    private final boolean methods;
    
    public ParallelComputer(final boolean classes, final boolean methods) {
        this.classes = classes;
        this.methods = methods;
    }
    
    public static Computer classes() {
        return new ParallelComputer(true, false);
    }
    
    public static Computer methods() {
        return new ParallelComputer(false, true);
    }
    
    private static Runner parallelize(final Runner runner) {
        if (runner instanceof ParentRunner) {
            ((ParentRunner)runner).setScheduler(new RunnerScheduler() {
                private final ExecutorService fService = Executors.newCachedThreadPool();
                
                public void schedule(final Runnable childStatement) {
                    this.fService.submit(childStatement);
                }
                
                public void finished() {
                    try {
                        this.fService.shutdown();
                        this.fService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                    }
                }
            });
        }
        return runner;
    }
    
    @Override
    public Runner getSuite(final RunnerBuilder builder, final Class<?>[] classes) throws InitializationError {
        final Runner suite = super.getSuite(builder, classes);
        return this.classes ? parallelize(suite) : suite;
    }
    
    @Override
    protected Runner getRunner(final RunnerBuilder builder, final Class<?> testClass) throws Throwable {
        final Runner runner = super.getRunner(builder, testClass);
        return this.methods ? parallelize(runner) : runner;
    }
}
