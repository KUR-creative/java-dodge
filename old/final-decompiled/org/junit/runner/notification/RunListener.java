// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runner.notification;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;
import org.junit.runner.Result;
import org.junit.runner.Description;

public class RunListener
{
    public void testRunStarted(final Description description) throws Exception {
    }
    
    public void testRunFinished(final Result result) throws Exception {
    }
    
    public void testStarted(final Description description) throws Exception {
    }
    
    public void testFinished(final Description description) throws Exception {
    }
    
    public void testFailure(final Failure failure) throws Exception {
    }
    
    public void testAssumptionFailure(final Failure failure) {
    }
    
    public void testIgnored(final Description description) throws Exception {
    }
    
    @Documented
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ThreadSafe {
    }
}
