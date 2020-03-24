// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners.statements;

import java.util.Iterator;
import org.junit.runners.model.FrameworkMethod;
import java.util.List;
import org.junit.runners.model.Statement;

public class RunBefores extends Statement
{
    private final Statement next;
    private final Object target;
    private final List<FrameworkMethod> befores;
    
    public RunBefores(final Statement next, final List<FrameworkMethod> befores, final Object target) {
        this.next = next;
        this.befores = befores;
        this.target = target;
    }
    
    @Override
    public void evaluate() throws Throwable {
        for (final FrameworkMethod before : this.befores) {
            before.invokeExplosively(this.target, new Object[0]);
        }
        this.next.evaluate();
    }
}
