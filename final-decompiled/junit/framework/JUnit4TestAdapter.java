// 
// Decompiled by Procyon v0.5.36
// 

package junit.framework;

import org.junit.runner.manipulation.Sorter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Filter;
import org.junit.Ignore;
import java.util.Iterator;
import org.junit.runner.Description;
import java.util.List;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.Describable;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Filterable;

public class JUnit4TestAdapter implements Test, Filterable, Sortable, Describable
{
    private final Class<?> fNewTestClass;
    private final Runner fRunner;
    private final JUnit4TestAdapterCache fCache;
    
    public JUnit4TestAdapter(final Class<?> newTestClass) {
        this(newTestClass, JUnit4TestAdapterCache.getDefault());
    }
    
    public JUnit4TestAdapter(final Class<?> newTestClass, final JUnit4TestAdapterCache cache) {
        this.fCache = cache;
        this.fNewTestClass = newTestClass;
        this.fRunner = Request.classWithoutSuiteMethod(newTestClass).getRunner();
    }
    
    public int countTestCases() {
        return this.fRunner.testCount();
    }
    
    public void run(final TestResult result) {
        this.fRunner.run(this.fCache.getNotifier(result, this));
    }
    
    public List<Test> getTests() {
        return this.fCache.asTestList(this.getDescription());
    }
    
    public Class<?> getTestClass() {
        return this.fNewTestClass;
    }
    
    public Description getDescription() {
        final Description description = this.fRunner.getDescription();
        return this.removeIgnored(description);
    }
    
    private Description removeIgnored(final Description description) {
        if (this.isIgnored(description)) {
            return Description.EMPTY;
        }
        final Description result = description.childlessCopy();
        for (final Description each : description.getChildren()) {
            final Description child = this.removeIgnored(each);
            if (!child.isEmpty()) {
                result.addChild(child);
            }
        }
        return result;
    }
    
    private boolean isIgnored(final Description description) {
        return description.getAnnotation(Ignore.class) != null;
    }
    
    @Override
    public String toString() {
        return this.fNewTestClass.getName();
    }
    
    public void filter(final Filter filter) throws NoTestsRemainException {
        filter.apply(this.fRunner);
    }
    
    public void sort(final Sorter sorter) {
        sorter.apply(this.fRunner);
    }
}
