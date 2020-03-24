// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.results;

import org.junit.internal.TextListener;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import org.junit.runner.notification.Failure;
import java.util.List;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

public class PrintableResult
{
    private Result result;
    
    public static PrintableResult testResult(final Class<?> type) {
        return testResult(Request.aClass(type));
    }
    
    public static PrintableResult testResult(final Request request) {
        return new PrintableResult(new JUnitCore().run(request));
    }
    
    public PrintableResult(final List<Failure> failures) {
        this(new FailureList(failures).result());
    }
    
    private PrintableResult(final Result result) {
        this.result = result;
    }
    
    public int failureCount() {
        return this.result.getFailures().size();
    }
    
    @Override
    public String toString() {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        new TextListener(new PrintStream(stream)).testRunFinished(this.result);
        return stream.toString();
    }
}
