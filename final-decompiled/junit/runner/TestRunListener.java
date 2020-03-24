// 
// Decompiled by Procyon v0.5.36
// 

package junit.runner;

public interface TestRunListener
{
    public static final int STATUS_ERROR = 1;
    public static final int STATUS_FAILURE = 2;
    
    void testRunStarted(final String p0, final int p1);
    
    void testRunEnded(final long p0);
    
    void testRunStopped(final long p0);
    
    void testStarted(final String p0);
    
    void testEnded(final String p0);
    
    void testFailed(final int p0, final String p1, final String p2);
}
