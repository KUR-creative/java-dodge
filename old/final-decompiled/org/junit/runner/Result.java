// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runner;

import java.util.Collections;
import java.util.ArrayList;
import java.io.ObjectStreamClass;
import org.junit.runner.notification.RunListener;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.runner.notification.Failure;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.ObjectStreamField;
import java.io.Serializable;

public class Result implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final ObjectStreamField[] serialPersistentFields;
    private final AtomicInteger count;
    private final AtomicInteger ignoreCount;
    private final CopyOnWriteArrayList<Failure> failures;
    private final AtomicLong runTime;
    private final AtomicLong startTime;
    private SerializedForm serializedForm;
    
    public Result() {
        this.count = new AtomicInteger();
        this.ignoreCount = new AtomicInteger();
        this.failures = new CopyOnWriteArrayList<Failure>();
        this.runTime = new AtomicLong();
        this.startTime = new AtomicLong();
    }
    
    private Result(final SerializedForm serializedForm) {
        this.count = serializedForm.fCount;
        this.ignoreCount = serializedForm.fIgnoreCount;
        this.failures = new CopyOnWriteArrayList<Failure>(serializedForm.fFailures);
        this.runTime = new AtomicLong(serializedForm.fRunTime);
        this.startTime = new AtomicLong(serializedForm.fStartTime);
    }
    
    public int getRunCount() {
        return this.count.get();
    }
    
    public int getFailureCount() {
        return this.failures.size();
    }
    
    public long getRunTime() {
        return this.runTime.get();
    }
    
    public List<Failure> getFailures() {
        return this.failures;
    }
    
    public int getIgnoreCount() {
        return this.ignoreCount.get();
    }
    
    public boolean wasSuccessful() {
        return this.getFailureCount() == 0;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final SerializedForm serializedForm = new SerializedForm(this);
        serializedForm.serialize(s);
    }
    
    private void readObject(final ObjectInputStream s) throws ClassNotFoundException, IOException {
        this.serializedForm = SerializedForm.deserialize(s);
    }
    
    private Object readResolve() {
        return new Result(this.serializedForm);
    }
    
    public RunListener createListener() {
        return new Listener();
    }
    
    static {
        serialPersistentFields = ObjectStreamClass.lookup(SerializedForm.class).getFields();
    }
    
    @ThreadSafe
    private class Listener extends RunListener
    {
        @Override
        public void testRunStarted(final Description description) throws Exception {
            Result.this.startTime.set(System.currentTimeMillis());
        }
        
        @Override
        public void testRunFinished(final Result result) throws Exception {
            final long endTime = System.currentTimeMillis();
            Result.this.runTime.addAndGet(endTime - Result.this.startTime.get());
        }
        
        @Override
        public void testFinished(final Description description) throws Exception {
            Result.this.count.getAndIncrement();
        }
        
        @Override
        public void testFailure(final Failure failure) throws Exception {
            Result.this.failures.add(failure);
        }
        
        @Override
        public void testIgnored(final Description description) throws Exception {
            Result.this.ignoreCount.getAndIncrement();
        }
        
        @Override
        public void testAssumptionFailure(final Failure failure) {
        }
    }
    
    private static class SerializedForm implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private final AtomicInteger fCount;
        private final AtomicInteger fIgnoreCount;
        private final List<Failure> fFailures;
        private final long fRunTime;
        private final long fStartTime;
        
        public SerializedForm(final Result result) {
            this.fCount = result.count;
            this.fIgnoreCount = result.ignoreCount;
            this.fFailures = Collections.synchronizedList(new ArrayList<Failure>(result.failures));
            this.fRunTime = result.runTime.longValue();
            this.fStartTime = result.startTime.longValue();
        }
        
        private SerializedForm(final ObjectInputStream.GetField fields) throws IOException {
            this.fCount = (AtomicInteger)fields.get("fCount", null);
            this.fIgnoreCount = (AtomicInteger)fields.get("fIgnoreCount", null);
            this.fFailures = (List<Failure>)fields.get("fFailures", null);
            this.fRunTime = fields.get("fRunTime", 0L);
            this.fStartTime = fields.get("fStartTime", 0L);
        }
        
        public void serialize(final ObjectOutputStream s) throws IOException {
            final ObjectOutputStream.PutField fields = s.putFields();
            fields.put("fCount", this.fCount);
            fields.put("fIgnoreCount", this.fIgnoreCount);
            fields.put("fFailures", this.fFailures);
            fields.put("fRunTime", this.fRunTime);
            fields.put("fStartTime", this.fStartTime);
            s.writeFields();
        }
        
        public static SerializedForm deserialize(final ObjectInputStream s) throws ClassNotFoundException, IOException {
            final ObjectInputStream.GetField fields = s.readFields();
            return new SerializedForm(fields);
        }
    }
}
