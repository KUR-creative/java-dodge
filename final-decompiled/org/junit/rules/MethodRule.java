// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.rules;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public interface MethodRule
{
    Statement apply(final Statement p0, final FrameworkMethod p1, final Object p2);
}
