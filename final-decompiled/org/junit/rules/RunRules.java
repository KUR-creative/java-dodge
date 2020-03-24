// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.rules;

import java.util.Iterator;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RunRules extends Statement
{
    private final Statement statement;
    
    public RunRules(final Statement base, final Iterable<TestRule> rules, final Description description) {
        this.statement = applyAll(base, rules, description);
    }
    
    @Override
    public void evaluate() throws Throwable {
        this.statement.evaluate();
    }
    
    private static Statement applyAll(Statement result, final Iterable<TestRule> rules, final Description description) {
        for (final TestRule each : rules) {
            result = each.apply(result, description);
        }
        return result;
    }
}
