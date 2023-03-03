// 
// Decompiled by Procyon v0.5.36
// 

package junit.extensions;

import junit.framework.TestResult;
import junit.framework.Test;
import junit.framework.Assert;

public class TestDecorator extends Assert implements Test
{
    protected Test fTest;
    
    public TestDecorator(final Test test) {
        this.fTest = test;
    }
    
    public void basicRun(final TestResult result) {
        this.fTest.run(result);
    }
    
    public int countTestCases() {
        return this.fTest.countTestCases();
    }
    
    public void run(final TestResult result) {
        this.basicRun(result);
    }
    
    @Override
    public String toString() {
        return this.fTest.toString();
    }
    
    public Test getTest() {
        return this.fTest;
    }
}
