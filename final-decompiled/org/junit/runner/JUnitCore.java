// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runner;

import org.junit.internal.runners.JUnit38ClassRunner;
import junit.framework.Test;
import org.junit.runner.notification.RunListener;
import org.junit.internal.TextListener;
import junit.runner.Version;
import org.junit.internal.JUnitSystem;
import org.junit.internal.RealSystem;
import org.junit.runner.notification.RunNotifier;

public class JUnitCore
{
    private final RunNotifier notifier;
    
    public JUnitCore() {
        this.notifier = new RunNotifier();
    }
    
    public static void main(final String... args) {
        final Result result = new JUnitCore().runMain(new RealSystem(), args);
        System.exit(result.wasSuccessful() ? 0 : 1);
    }
    
    public static Result runClasses(final Class<?>... classes) {
        return runClasses(defaultComputer(), classes);
    }
    
    public static Result runClasses(final Computer computer, final Class<?>... classes) {
        return new JUnitCore().run(computer, classes);
    }
    
    Result runMain(final JUnitSystem system, final String... args) {
        system.out().println("JUnit version " + Version.id());
        final JUnitCommandLineParseResult jUnitCommandLineParseResult = JUnitCommandLineParseResult.parse(args);
        final RunListener listener = new TextListener(system);
        this.addListener(listener);
        return this.run(jUnitCommandLineParseResult.createRequest(defaultComputer()));
    }
    
    public String getVersion() {
        return Version.id();
    }
    
    public Result run(final Class<?>... classes) {
        return this.run(defaultComputer(), classes);
    }
    
    public Result run(final Computer computer, final Class<?>... classes) {
        return this.run(Request.classes(computer, classes));
    }
    
    public Result run(final Request request) {
        return this.run(request.getRunner());
    }
    
    public Result run(final Test test) {
        return this.run(new JUnit38ClassRunner(test));
    }
    
    public Result run(final Runner runner) {
        final Result result = new Result();
        final RunListener listener = result.createListener();
        this.notifier.addFirstListener(listener);
        try {
            this.notifier.fireTestRunStarted(runner.getDescription());
            runner.run(this.notifier);
            this.notifier.fireTestRunFinished(result);
        }
        finally {
            this.removeListener(listener);
        }
        return result;
    }
    
    public void addListener(final RunListener listener) {
        this.notifier.addListener(listener);
    }
    
    public void removeListener(final RunListener listener) {
        this.notifier.removeListener(listener);
    }
    
    static Computer defaultComputer() {
        return new Computer();
    }
}
