// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners.statements;

import java.util.Iterator;
import org.junit.runners.model.MultipleFailureException;
import java.util.ArrayList;
import org.junit.runners.model.FrameworkMethod;
import java.util.List;
import org.junit.runners.model.Statement;

public class RunAfters extends Statement
{
    private final Statement next;
    private final Object target;
    private final List<FrameworkMethod> afters;
    
    public RunAfters(final Statement next, final List<FrameworkMethod> afters, final Object target) {
        this.next = next;
        this.afters = afters;
        this.target = target;
    }
    
    @Override
    public void evaluate() throws Throwable {
        final List<Throwable> errors = new ArrayList<Throwable>();
        try {
            this.next.evaluate();
        }
        catch (Throwable e) {
            errors.add(e);
        }
        finally {
            for (final FrameworkMethod each : this.afters) {
                try {
                    each.invokeExplosively(this.target, new Object[0]);
                }
                catch (Throwable e2) {
                    errors.add(e2);
                }
            }
        }
        MultipleFailureException.assertEmpty(errors);
    }
}
