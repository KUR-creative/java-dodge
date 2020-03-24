// 
// Decompiled by Procyon v0.5.36
// 

package junit.extensions;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ActiveTestSuite extends TestSuite
{
    private volatile int fActiveTestDeathCount;
    
    public ActiveTestSuite() {
    }
    
    public ActiveTestSuite(final Class<? extends TestCase> theClass) {
        super(theClass);
    }
    
    public ActiveTestSuite(final String name) {
        super(name);
    }
    
    public ActiveTestSuite(final Class<? extends TestCase> theClass, final String name) {
        super(theClass, name);
    }
    
    @Override
    public void run(final TestResult result) {
        this.fActiveTestDeathCount = 0;
        super.run(result);
        this.waitUntilFinished();
    }
    
    @Override
    public void runTest(final Test test, final TestResult result) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    test.run(result);
                }
                finally {
                    ActiveTestSuite.this.runFinished();
                }
            }
        };
        t.start();
    }
    
    synchronized void waitUntilFinished() {
        while (this.fActiveTestDeathCount < this.testCount()) {
            try {
                this.wait();
                continue;
            }
            catch (InterruptedException e) {
                return;
            }
            break;
        }
    }
    
    public synchronized void runFinished() {
        ++this.fActiveTestDeathCount;
        this.notifyAll();
    }
}
