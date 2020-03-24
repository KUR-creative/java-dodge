// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import org.junit.runner.notification.Failure;
import org.junit.runner.Description;
import org.junit.runner.Result;
import java.io.PrintStream;
import org.junit.runner.notification.RunListener;

public class TextListener extends RunListener
{
    private final PrintStream writer;
    
    public TextListener(final JUnitSystem system) {
        this(system.out());
    }
    
    public TextListener(final PrintStream writer) {
        this.writer = writer;
    }
    
    @Override
    public void testRunFinished(final Result result) {
        this.printHeader(result.getRunTime());
        this.printFailures(result);
        this.printFooter(result);
    }
    
    @Override
    public void testStarted(final Description description) {
        this.writer.append('.');
    }
    
    @Override
    public void testFailure(final Failure failure) {
        this.writer.append('E');
    }
    
    @Override
    public void testIgnored(final Description description) {
        this.writer.append('I');
    }
    
    private PrintStream getWriter() {
        return this.writer;
    }
    
    protected void printHeader(final long runTime) {
        this.getWriter().println();
        this.getWriter().println("Time: " + this.elapsedTimeAsString(runTime));
    }
    
    protected void printFailures(final Result result) {
        final List<Failure> failures = result.getFailures();
        if (failures.size() == 0) {
            return;
        }
        if (failures.size() == 1) {
            this.getWriter().println("There was " + failures.size() + " failure:");
        }
        else {
            this.getWriter().println("There were " + failures.size() + " failures:");
        }
        int i = 1;
        for (final Failure each : failures) {
            this.printFailure(each, "" + i++);
        }
    }
    
    protected void printFailure(final Failure each, final String prefix) {
        this.getWriter().println(prefix + ") " + each.getTestHeader());
        this.getWriter().print(each.getTrace());
    }
    
    protected void printFooter(final Result result) {
        if (result.wasSuccessful()) {
            this.getWriter().println();
            this.getWriter().print("OK");
            this.getWriter().println(" (" + result.getRunCount() + " test" + ((result.getRunCount() == 1) ? "" : "s") + ")");
        }
        else {
            this.getWriter().println();
            this.getWriter().println("FAILURES!!!");
            this.getWriter().println("Tests run: " + result.getRunCount() + ",  Failures: " + result.getFailureCount());
        }
        this.getWriter().println();
    }
    
    protected String elapsedTimeAsString(final long runTime) {
        return NumberFormat.getInstance().format(runTime / 1000.0);
    }
}
