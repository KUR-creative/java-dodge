// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners.statements;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class InvokeMethod extends Statement
{
    private final FrameworkMethod testMethod;
    private final Object target;
    
    public InvokeMethod(final FrameworkMethod testMethod, final Object target) {
        this.testMethod = testMethod;
        this.target = target;
    }
    
    @Override
    public void evaluate() throws Throwable {
        this.testMethod.invokeExplosively(this.target, new Object[0]);
    }
}
