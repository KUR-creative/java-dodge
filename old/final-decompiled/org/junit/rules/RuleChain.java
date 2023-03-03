// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.rules;

import java.util.Collections;
import java.util.Iterator;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class RuleChain implements TestRule
{
    private static final RuleChain EMPTY_CHAIN;
    private List<TestRule> rulesStartingWithInnerMost;
    
    public static RuleChain emptyRuleChain() {
        return RuleChain.EMPTY_CHAIN;
    }
    
    public static RuleChain outerRule(final TestRule outerRule) {
        return emptyRuleChain().around(outerRule);
    }
    
    private RuleChain(final List<TestRule> rules) {
        this.rulesStartingWithInnerMost = rules;
    }
    
    public RuleChain around(final TestRule enclosedRule) {
        final List<TestRule> rulesOfNewChain = new ArrayList<TestRule>();
        rulesOfNewChain.add(enclosedRule);
        rulesOfNewChain.addAll(this.rulesStartingWithInnerMost);
        return new RuleChain(rulesOfNewChain);
    }
    
    public Statement apply(Statement base, final Description description) {
        for (final TestRule each : this.rulesStartingWithInnerMost) {
            base = each.apply(base, description);
        }
        return base;
    }
    
    static {
        EMPTY_CHAIN = new RuleChain(Collections.emptyList());
    }
}
