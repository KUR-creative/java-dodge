// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.rules;

import org.junit.runner.Description;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.runners.model.Statement;
import java.util.concurrent.TimeUnit;

public class Timeout implements TestRule
{
    private final long timeout;
    private final TimeUnit timeUnit;
    private final boolean lookForStuckThread;
    
    public static Builder builder() {
        return new Builder();
    }
    
    @Deprecated
    public Timeout(final int millis) {
        this(millis, TimeUnit.MILLISECONDS);
    }
    
    public Timeout(final long timeout, final TimeUnit timeUnit) {
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.lookForStuckThread = false;
    }
    
    protected Timeout(final Builder builder) {
        this.timeout = builder.getTimeout();
        this.timeUnit = builder.getTimeUnit();
        this.lookForStuckThread = builder.getLookingForStuckThread();
    }
    
    public static Timeout millis(final long millis) {
        return new Timeout(millis, TimeUnit.MILLISECONDS);
    }
    
    public static Timeout seconds(final long seconds) {
        return new Timeout(seconds, TimeUnit.SECONDS);
    }
    
    protected final long getTimeout(final TimeUnit unit) {
        return unit.convert(this.timeout, this.timeUnit);
    }
    
    protected final boolean getLookingForStuckThread() {
        return this.lookForStuckThread;
    }
    
    protected Statement createFailOnTimeoutStatement(final Statement statement) throws Exception {
        return FailOnTimeout.builder().withTimeout(this.timeout, this.timeUnit).withLookingForStuckThread(this.lookForStuckThread).build(statement);
    }
    
    public Statement apply(final Statement base, final Description description) {
        try {
            return this.createFailOnTimeoutStatement(base);
        }
        catch (Exception e) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    throw new RuntimeException("Invalid parameters for Timeout", e);
                }
            };
        }
    }
    
    public static class Builder
    {
        private boolean lookForStuckThread;
        private long timeout;
        private TimeUnit timeUnit;
        
        protected Builder() {
            this.lookForStuckThread = false;
            this.timeout = 0L;
            this.timeUnit = TimeUnit.SECONDS;
        }
        
        public Builder withTimeout(final long timeout, final TimeUnit unit) {
            this.timeout = timeout;
            this.timeUnit = unit;
            return this;
        }
        
        protected long getTimeout() {
            return this.timeout;
        }
        
        protected TimeUnit getTimeUnit() {
            return this.timeUnit;
        }
        
        public Builder withLookingForStuckThread(final boolean enable) {
            this.lookForStuckThread = enable;
            return this;
        }
        
        protected boolean getLookingForStuckThread() {
            return this.lookForStuckThread;
        }
        
        public Timeout build() {
            return new Timeout(this);
        }
    }
}
