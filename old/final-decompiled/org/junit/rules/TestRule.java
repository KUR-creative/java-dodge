// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public interface TestRule
{
    Statement apply(final Statement p0, final Description p1);
}
