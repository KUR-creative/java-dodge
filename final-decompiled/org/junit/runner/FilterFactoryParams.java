// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runner;

public final class FilterFactoryParams
{
    private final Description topLevelDescription;
    private final String args;
    
    public FilterFactoryParams(final Description topLevelDescription, final String args) {
        if (args == null || topLevelDescription == null) {
            throw new NullPointerException();
        }
        this.topLevelDescription = topLevelDescription;
        this.args = args;
    }
    
    public String getArgs() {
        return this.args;
    }
    
    public Description getTopLevelDescription() {
        return this.topLevelDescription;
    }
}
