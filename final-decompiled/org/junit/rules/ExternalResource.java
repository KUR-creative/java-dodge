// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public abstract class ExternalResource implements TestRule
{
    public Statement apply(final Statement base, final Description description) {
        return this.statement(base);
    }
    
    private Statement statement(final Statement base) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                ExternalResource.this.before();
                try {
                    base.evaluate();
                }
                finally {
                    ExternalResource.this.after();
                }
            }
        };
    }
    
    protected void before() throws Throwable {
    }
    
    protected void after() {
    }
}
