// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal;

public final class Throwables
{
    private Throwables() {
    }
    
    public static Exception rethrowAsException(final Throwable e) throws Exception {
        rethrow(e);
        return null;
    }
    
    private static <T extends Throwable> void rethrow(final Throwable e) throws T, Throwable {
        throw e;
    }
}
