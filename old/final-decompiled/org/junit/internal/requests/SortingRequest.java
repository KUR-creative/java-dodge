// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.requests;

import org.junit.runner.manipulation.Sorter;
import org.junit.runner.Runner;
import org.junit.runner.Description;
import java.util.Comparator;
import org.junit.runner.Request;

public class SortingRequest extends Request
{
    private final Request request;
    private final Comparator<Description> comparator;
    
    public SortingRequest(final Request request, final Comparator<Description> comparator) {
        this.request = request;
        this.comparator = comparator;
    }
    
    @Override
    public Runner getRunner() {
        final Runner runner = this.request.getRunner();
        new Sorter(this.comparator).apply(runner);
        return runner;
    }
}
