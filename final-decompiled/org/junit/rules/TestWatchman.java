// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.rules;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

@Deprecated
public class TestWatchman implements MethodRule
{
    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                TestWatchman.this.starting(method);
                try {
                    base.evaluate();
                    TestWatchman.this.succeeded(method);
                }
                catch (AssumptionViolatedException e) {
                    throw e;
                }
                catch (Throwable e2) {
                    TestWatchman.this.failed(e2, method);
                    throw e2;
                }
                finally {
                    TestWatchman.this.finished(method);
                }
            }
        };
    }
    
    public void succeeded(final FrameworkMethod method) {
    }
    
    public void failed(final Throwable e, final FrameworkMethod method) {
    }
    
    public void starting(final FrameworkMethod method) {
    }
    
    public void finished(final FrameworkMethod method) {
    }
}
