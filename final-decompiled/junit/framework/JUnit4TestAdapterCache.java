// 
// Decompiled by Procyon v0.5.36
// 

package junit.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import java.util.Iterator;
import org.junit.runner.Description;
import java.util.HashMap;

public class JUnit4TestAdapterCache extends HashMap<Description, Test>
{
    private static final long serialVersionUID = 1L;
    private static final JUnit4TestAdapterCache fInstance;
    
    public static JUnit4TestAdapterCache getDefault() {
        return JUnit4TestAdapterCache.fInstance;
    }
    
    public Test asTest(final Description description) {
        if (description.isSuite()) {
            return this.createTest(description);
        }
        if (!this.containsKey(description)) {
            this.put(description, this.createTest(description));
        }
        return ((HashMap<K, Test>)this).get(description);
    }
    
    Test createTest(final Description description) {
        if (description.isTest()) {
            return new JUnit4TestCaseFacade(description);
        }
        final TestSuite suite = new TestSuite(description.getDisplayName());
        for (final Description child : description.getChildren()) {
            suite.addTest(this.asTest(child));
        }
        return suite;
    }
    
    public RunNotifier getNotifier(final TestResult result, final JUnit4TestAdapter adapter) {
        final RunNotifier notifier = new RunNotifier();
        notifier.addListener(new RunListener() {
            @Override
            public void testFailure(final Failure failure) throws Exception {
                result.addError(JUnit4TestAdapterCache.this.asTest(failure.getDescription()), failure.getException());
            }
            
            @Override
            public void testFinished(final Description description) throws Exception {
                result.endTest(JUnit4TestAdapterCache.this.asTest(description));
            }
            
            @Override
            public void testStarted(final Description description) throws Exception {
                result.startTest(JUnit4TestAdapterCache.this.asTest(description));
            }
        });
        return notifier;
    }
    
    public List<Test> asTestList(final Description description) {
        if (description.isTest()) {
            return Arrays.asList(this.asTest(description));
        }
        final List<Test> returnThis = new ArrayList<Test>();
        for (final Description child : description.getChildren()) {
            returnThis.add(this.asTest(child));
        }
        return returnThis;
    }
    
    static {
        fInstance = new JUnit4TestAdapterCache();
    }
}
