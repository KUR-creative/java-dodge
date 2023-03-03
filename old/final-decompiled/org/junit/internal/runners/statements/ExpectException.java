// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners.statements;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.model.Statement;

public class ExpectException extends Statement
{
    private final Statement next;
    private final Class<? extends Throwable> expected;
    
    public ExpectException(final Statement next, final Class<? extends Throwable> expected) {
        this.next = next;
        this.expected = expected;
    }
    
    @Override
    public void evaluate() throws Exception {
        boolean complete = false;
        try {
            this.next.evaluate();
            complete = true;
        }
        catch (AssumptionViolatedException e) {
            throw e;
        }
        catch (Throwable e2) {
            if (!this.expected.isAssignableFrom(e2.getClass())) {
                final String message = "Unexpected exception, expected<" + this.expected.getName() + "> but was<" + e2.getClass().getName() + ">";
                throw new Exception(message, e2);
            }
        }
        if (complete) {
            throw new AssertionError((Object)("Expected exception: " + this.expected.getName()));
        }
    }
}
