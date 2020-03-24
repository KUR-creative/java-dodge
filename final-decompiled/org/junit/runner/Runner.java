// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runner;

import org.junit.runner.notification.RunNotifier;

public abstract class Runner implements Describable
{
    public abstract Description getDescription();
    
    public abstract void run(final RunNotifier p0);
    
    public int testCount() {
        return this.getDescription().testCount();
    }
}
