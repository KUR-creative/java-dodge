// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners.statements;

import java.util.concurrent.CountDownLatch;
import java.lang.management.ThreadMXBean;
import java.lang.management.ManagementFactory;
import org.junit.runners.model.MultipleFailureException;
import java.util.Arrays;
import org.junit.runners.model.TestTimedOutException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.junit.runners.model.Statement;

public class FailOnTimeout extends Statement
{
    private final Statement originalStatement;
    private final TimeUnit timeUnit;
    private final long timeout;
    private final boolean lookForStuckThread;
    private volatile ThreadGroup threadGroup;
    
    public static Builder builder() {
        return new Builder();
    }
    
    @Deprecated
    public FailOnTimeout(final Statement statement, final long timeoutMillis) {
        this(builder().withTimeout(timeoutMillis, TimeUnit.MILLISECONDS), statement);
    }
    
    private FailOnTimeout(final Builder builder, final Statement statement) {
        this.threadGroup = null;
        this.originalStatement = statement;
        this.timeout = builder.timeout;
        this.timeUnit = builder.unit;
        this.lookForStuckThread = builder.lookForStuckThread;
    }
    
    @Override
    public void evaluate() throws Throwable {
        final CallableStatement callable = new CallableStatement();
        final FutureTask<Throwable> task = new FutureTask<Throwable>(callable);
        this.threadGroup = new ThreadGroup("FailOnTimeoutGroup");
        final Thread thread = new Thread(this.threadGroup, task, "Time-limited test");
        thread.setDaemon(true);
        thread.start();
        callable.awaitStarted();
        final Throwable throwable = this.getResult(task, thread);
        if (throwable != null) {
            throw throwable;
        }
    }
    
    private Throwable getResult(final FutureTask<Throwable> task, final Thread thread) {
        try {
            if (this.timeout > 0L) {
                return task.get(this.timeout, this.timeUnit);
            }
            return task.get();
        }
        catch (InterruptedException e) {
            return e;
        }
        catch (ExecutionException e2) {
            return e2.getCause();
        }
        catch (TimeoutException e3) {
            return this.createTimeoutException(thread);
        }
    }
    
    private Exception createTimeoutException(final Thread thread) {
        final StackTraceElement[] stackTrace = thread.getStackTrace();
        final Thread stuckThread = this.lookForStuckThread ? this.getStuckThread(thread) : null;
        final Exception currThreadException = new TestTimedOutException(this.timeout, this.timeUnit);
        if (stackTrace != null) {
            currThreadException.setStackTrace(stackTrace);
            thread.interrupt();
        }
        if (stuckThread != null) {
            final Exception stuckThreadException = new Exception("Appears to be stuck in thread " + stuckThread.getName());
            stuckThreadException.setStackTrace(this.getStackTrace(stuckThread));
            return new MultipleFailureException(Arrays.asList(currThreadException, stuckThreadException));
        }
        return currThreadException;
    }
    
    private StackTraceElement[] getStackTrace(final Thread thread) {
        try {
            return thread.getStackTrace();
        }
        catch (SecurityException e) {
            return new StackTraceElement[0];
        }
    }
    
    private Thread getStuckThread(final Thread mainThread) {
        if (this.threadGroup == null) {
            return null;
        }
        final Thread[] threadsInGroup = this.getThreadArray(this.threadGroup);
        if (threadsInGroup == null) {
            return null;
        }
        Thread stuckThread = null;
        long maxCpuTime = 0L;
        for (final Thread thread : threadsInGroup) {
            if (thread.getState() == Thread.State.RUNNABLE) {
                final long threadCpuTime = this.cpuTime(thread);
                if (stuckThread == null || threadCpuTime > maxCpuTime) {
                    stuckThread = thread;
                    maxCpuTime = threadCpuTime;
                }
            }
        }
        return (stuckThread == mainThread) ? null : stuckThread;
    }
    
    private Thread[] getThreadArray(final ThreadGroup group) {
        final int count = group.activeCount();
        int enumSize = Math.max(count * 2, 100);
        int loopCount = 0;
        do {
            final Thread[] threads = new Thread[enumSize];
            final int enumCount = group.enumerate(threads);
            if (enumCount < enumSize) {
                return this.copyThreads(threads, enumCount);
            }
            enumSize += 100;
        } while (++loopCount < 5);
        return null;
    }
    
    private Thread[] copyThreads(final Thread[] threads, final int count) {
        final int length = Math.min(count, threads.length);
        final Thread[] result = new Thread[length];
        for (int i = 0; i < length; ++i) {
            result[i] = threads[i];
        }
        return result;
    }
    
    private long cpuTime(final Thread thr) {
        final ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        if (mxBean.isThreadCpuTimeSupported()) {
            try {
                return mxBean.getThreadCpuTime(thr.getId());
            }
            catch (UnsupportedOperationException ex) {}
        }
        return 0L;
    }
    
    public static class Builder
    {
        private boolean lookForStuckThread;
        private long timeout;
        private TimeUnit unit;
        
        private Builder() {
            this.lookForStuckThread = false;
            this.timeout = 0L;
            this.unit = TimeUnit.SECONDS;
        }
        
        public Builder withTimeout(final long timeout, final TimeUnit unit) {
            if (timeout < 0L) {
                throw new IllegalArgumentException("timeout must be non-negative");
            }
            if (unit == null) {
                throw new NullPointerException("TimeUnit cannot be null");
            }
            this.timeout = timeout;
            this.unit = unit;
            return this;
        }
        
        public Builder withLookingForStuckThread(final boolean enable) {
            this.lookForStuckThread = enable;
            return this;
        }
        
        public FailOnTimeout build(final Statement statement) {
            if (statement == null) {
                throw new NullPointerException("statement cannot be null");
            }
            return new FailOnTimeout(this, statement, null);
        }
    }
    
    private class CallableStatement implements Callable<Throwable>
    {
        private final CountDownLatch startLatch;
        
        private CallableStatement() {
            this.startLatch = new CountDownLatch(1);
        }
        
        public Throwable call() throws Exception {
            try {
                this.startLatch.countDown();
                FailOnTimeout.this.originalStatement.evaluate();
            }
            catch (Exception e) {
                throw e;
            }
            catch (Throwable e2) {
                return e2;
            }
            return null;
        }
        
        public void awaitStarted() throws InterruptedException {
            this.startLatch.await();
        }
    }
}
