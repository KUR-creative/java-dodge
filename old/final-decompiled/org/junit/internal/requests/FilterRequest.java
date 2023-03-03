// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.requests;

import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.Request;

public final class FilterRequest extends Request
{
    private final Request request;
    private final Filter fFilter;
    
    public FilterRequest(final Request request, final Filter filter) {
        this.request = request;
        this.fFilter = filter;
    }
    
    @Override
    public Runner getRunner() {
        try {
            final Runner runner = this.request.getRunner();
            this.fFilter.apply(runner);
            return runner;
        }
        catch (NoTestsRemainException e) {
            return new ErrorReportingRunner(Filter.class, new Exception(String.format("No tests found matching %s from %s", this.fFilter.describe(), this.request.toString())));
        }
    }
}
