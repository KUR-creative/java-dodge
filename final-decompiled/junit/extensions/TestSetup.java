// 
// Decompiled by Procyon v0.5.36
// 

package junit.extensions;

import junit.framework.Protectable;
import junit.framework.TestResult;
import junit.framework.Test;

public class TestSetup extends TestDecorator
{
    public TestSetup(final Test test) {
        super(test);
    }
    
    @Override
    public void run(final TestResult result) {
        final Protectable p = new Protectable() {
            public void protect() throws Exception {
                TestSetup.this.setUp();
                TestSetup.this.basicRun(result);
                TestSetup.this.tearDown();
            }
        };
        result.runProtected(this, p);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
}
