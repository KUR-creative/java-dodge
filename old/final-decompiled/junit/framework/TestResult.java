// 
// Decompiled by Procyon v0.5.36
// 

package junit.framework;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class TestResult
{
    protected List<TestFailure> fFailures;
    protected List<TestFailure> fErrors;
    protected List<TestListener> fListeners;
    protected int fRunTests;
    private boolean fStop;
    
    public TestResult() {
        this.fFailures = new ArrayList<TestFailure>();
        this.fErrors = new ArrayList<TestFailure>();
        this.fListeners = new ArrayList<TestListener>();
        this.fRunTests = 0;
        this.fStop = false;
    }
    
    public synchronized void addError(final Test test, final Throwable e) {
        this.fErrors.add(new TestFailure(test, e));
        for (final TestListener each : this.cloneListeners()) {
            each.addError(test, e);
        }
    }
    
    public synchronized void addFailure(final Test test, final AssertionFailedError e) {
        this.fFailures.add(new TestFailure(test, e));
        for (final TestListener each : this.cloneListeners()) {
            each.addFailure(test, e);
        }
    }
    
    public synchronized void addListener(final TestListener listener) {
        this.fListeners.add(listener);
    }
    
    public synchronized void removeListener(final TestListener listener) {
        this.fListeners.remove(listener);
    }
    
    private synchronized List<TestListener> cloneListeners() {
        final List<TestListener> result = new ArrayList<TestListener>();
        result.addAll(this.fListeners);
        return result;
    }
    
    public void endTest(final Test test) {
        for (final TestListener each : this.cloneListeners()) {
            each.endTest(test);
        }
    }
    
    public synchronized int errorCount() {
        return this.fErrors.size();
    }
    
    public synchronized Enumeration<TestFailure> errors() {
        return Collections.enumeration(this.fErrors);
    }
    
    public synchronized int failureCount() {
        return this.fFailures.size();
    }
    
    public synchronized Enumeration<TestFailure> failures() {
        return Collections.enumeration(this.fFailures);
    }
    
    protected void run(final TestCase test) {
        this.startTest(test);
        final Protectable p = new Protectable() {
            public void protect() throws Throwable {
                test.runBare();
            }
        };
        this.runProtected(test, p);
        this.endTest(test);
    }
    
    public synchronized int runCount() {
        return this.fRunTests;
    }
    
    public void runProtected(final Test test, final Protectable p) {
        try {
            p.protect();
        }
        catch (AssertionFailedError e) {
            this.addFailure(test, e);
        }
        catch (ThreadDeath e2) {
            throw e2;
        }
        catch (Throwable e3) {
            this.addError(test, e3);
        }
    }
    
    public synchronized boolean shouldStop() {
        return this.fStop;
    }
    
    public void startTest(final Test test) {
        final int count = test.countTestCases();
        synchronized (this) {
            this.fRunTests += count;
        }
        for (final TestListener each : this.cloneListeners()) {
            each.startTest(test);
        }
    }
    
    public synchronized void stop() {
        this.fStop = true;
    }
    
    public synchronized boolean wasSuccessful() {
        return this.failureCount() == 0 && this.errorCount() == 0;
    }
}
