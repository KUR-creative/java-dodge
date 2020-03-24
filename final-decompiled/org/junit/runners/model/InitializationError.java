// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners.model;

import java.util.Arrays;
import java.util.List;

public class InitializationError extends Exception
{
    private static final long serialVersionUID = 1L;
    private final List<Throwable> fErrors;
    
    public InitializationError(final List<Throwable> errors) {
        this.fErrors = errors;
    }
    
    public InitializationError(final Throwable error) {
        this(Arrays.asList(error));
    }
    
    public InitializationError(final String string) {
        this(new Exception(string));
    }
    
    public List<Throwable> getCauses() {
        return this.fErrors;
    }
}
