// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.max;

import java.lang.annotation.Annotation;
import junit.framework.Test;
import org.junit.internal.runners.JUnit38ClassRunner;
import junit.framework.TestSuite;
import java.util.Iterator;
import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.Suite;
import org.junit.runner.Runner;
import java.util.ArrayList;
import java.util.List;
import org.junit.runner.Description;
import java.util.Comparator;
import java.util.Collections;
import org.junit.internal.requests.SortingRequest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import java.io.File;

public class MaxCore
{
    private static final String MALFORMED_JUNIT_3_TEST_CLASS_PREFIX = "malformed JUnit 3 test class: ";
    private final MaxHistory history;
    
    @Deprecated
    public static MaxCore forFolder(final String folderName) {
        return storedLocally(new File(folderName));
    }
    
    public static MaxCore storedLocally(final File storedResults) {
        return new MaxCore(storedResults);
    }
    
    private MaxCore(final File storedResults) {
        this.history = MaxHistory.forFolder(storedResults);
    }
    
    public Result run(final Class<?> testClass) {
        return this.run(Request.aClass(testClass));
    }
    
    public Result run(final Request request) {
        return this.run(request, new JUnitCore());
    }
    
    public Result run(final Request request, final JUnitCore core) {
        core.addListener(this.history.listener());
        return core.run(this.sortRequest(request).getRunner());
    }
    
    public Request sortRequest(final Request request) {
        if (request instanceof SortingRequest) {
            return request;
        }
        final List<Description> leaves = this.findLeaves(request);
        Collections.sort(leaves, this.history.testComparator());
        return this.constructLeafRequest(leaves);
    }
    
    private Request constructLeafRequest(final List<Description> leaves) {
        final List<Runner> runners = new ArrayList<Runner>();
        for (final Description each : leaves) {
            runners.add(this.buildRunner(each));
        }
        return new Request() {
            @Override
            public Runner getRunner() {
                try {
                    return new Suite((Class)null, runners) {};
                }
                catch (InitializationError e) {
                    return new ErrorReportingRunner(null, e);
                }
            }
        };
    }
    
    private Runner buildRunner(final Description each) {
        if (each.toString().equals("TestSuite with 0 tests")) {
            return Suite.emptySuite();
        }
        if (each.toString().startsWith("malformed JUnit 3 test class: ")) {
            return new JUnit38ClassRunner(new TestSuite(this.getMalformedTestClass(each)));
        }
        final Class<?> type = each.getTestClass();
        if (type == null) {
            throw new RuntimeException("Can't build a runner from description [" + each + "]");
        }
        final String methodName = each.getMethodName();
        if (methodName == null) {
            return Request.aClass(type).getRunner();
        }
        return Request.method(type, methodName).getRunner();
    }
    
    private Class<?> getMalformedTestClass(final Description each) {
        try {
            return Class.forName(each.toString().replace("malformed JUnit 3 test class: ", ""));
        }
        catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    public List<Description> sortedLeavesForTest(final Request request) {
        return this.findLeaves(this.sortRequest(request));
    }
    
    private List<Description> findLeaves(final Request request) {
        final List<Description> results = new ArrayList<Description>();
        this.findLeaves(null, request.getRunner().getDescription(), results);
        return results;
    }
    
    private void findLeaves(final Description parent, final Description description, final List<Description> results) {
        if (description.getChildren().isEmpty()) {
            if (description.toString().equals("warning(junit.framework.TestSuite$1)")) {
                results.add(Description.createSuiteDescription("malformed JUnit 3 test class: " + parent, new Annotation[0]));
            }
            else {
                results.add(description);
            }
        }
        else {
            for (final Description each : description.getChildren()) {
                this.findLeaves(description, each, results);
            }
        }
    }
}
