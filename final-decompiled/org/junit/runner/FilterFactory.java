// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runner;

import org.junit.runner.manipulation.Filter;

public interface FilterFactory
{
    Filter createFilter(final FilterFactoryParams p0) throws FilterNotCreatedException;
    
    public static class FilterNotCreatedException extends Exception
    {
        public FilterNotCreatedException(final Exception exception) {
            super(exception.getMessage(), exception);
        }
    }
}
