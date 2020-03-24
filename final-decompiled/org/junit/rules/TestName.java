// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.rules;

import org.junit.runner.Description;

public class TestName extends TestWatcher
{
    private String name;
    
    @Override
    protected void starting(final Description d) {
        this.name = d.getMethodName();
    }
    
    public String getMethodName() {
        return this.name;
    }
}
