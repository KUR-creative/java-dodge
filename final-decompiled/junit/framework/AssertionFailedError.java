// 
// Decompiled by Procyon v0.5.36
// 

package junit.framework;

public class AssertionFailedError extends AssertionError
{
    private static final long serialVersionUID = 1L;
    
    public AssertionFailedError() {
    }
    
    public AssertionFailedError(final String message) {
        super((Object)defaultString(message));
    }
    
    private static String defaultString(final String message) {
        return (message == null) ? "" : message;
    }
}
