// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal;

import java.io.PrintStream;

public class RealSystem implements JUnitSystem
{
    @Deprecated
    public void exit(final int code) {
        System.exit(code);
    }
    
    public PrintStream out() {
        return System.out;
    }
}
