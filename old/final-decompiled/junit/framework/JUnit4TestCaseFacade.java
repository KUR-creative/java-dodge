// 
// Decompiled by Procyon v0.5.36
// 

package junit.framework;

import org.junit.runner.Description;
import org.junit.runner.Describable;

public class JUnit4TestCaseFacade implements Test, Describable
{
    private final Description fDescription;
    
    JUnit4TestCaseFacade(final Description description) {
        this.fDescription = description;
    }
    
    @Override
    public String toString() {
        return this.getDescription().toString();
    }
    
    public int countTestCases() {
        return 1;
    }
    
    public void run(final TestResult result) {
        throw new RuntimeException("This test stub created only for informational purposes.");
    }
    
    public Description getDescription() {
        return this.fDescription;
    }
}
