// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.rules;

import java.util.Iterator;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import java.util.List;
import java.lang.management.ManagementFactory;

public class DisableOnDebug implements TestRule
{
    private final TestRule rule;
    private final boolean debugging;
    
    public DisableOnDebug(final TestRule rule) {
        this(rule, ManagementFactory.getRuntimeMXBean().getInputArguments());
    }
    
    DisableOnDebug(final TestRule rule, final List<String> inputArguments) {
        this.rule = rule;
        this.debugging = isDebugging(inputArguments);
    }
    
    public Statement apply(final Statement base, final Description description) {
        if (this.debugging) {
            return base;
        }
        return this.rule.apply(base, description);
    }
    
    private static boolean isDebugging(final List<String> arguments) {
        for (final String argument : arguments) {
            if ("-Xdebug".equals(argument)) {
                return true;
            }
            if (argument.startsWith("-agentlib:jdwp")) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isDebugging() {
        return this.debugging;
    }
}
