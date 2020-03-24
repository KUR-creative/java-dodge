// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners;

import java.util.Arrays;
import java.util.List;

@Deprecated
public class InitializationError extends Exception
{
    private static final long serialVersionUID = 1L;
    private final List<Throwable> fErrors;
    
    public InitializationError(final List<Throwable> errors) {
        this.fErrors = errors;
    }
    
    public InitializationError(final Throwable... errors) {
        this(Arrays.asList(errors));
    }
    
    public InitializationError(final String string) {
        this(new Throwable[] { new Exception(string) });
    }
    
    public List<Throwable> getCauses() {
        return this.fErrors;
    }
}
