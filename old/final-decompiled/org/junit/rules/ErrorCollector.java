// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.rules;

import org.junit.Assert;
import java.util.concurrent.Callable;
import org.hamcrest.Matcher;
import org.junit.runners.model.MultipleFailureException;
import java.util.ArrayList;
import java.util.List;

public class ErrorCollector extends Verifier
{
    private List<Throwable> errors;
    
    public ErrorCollector() {
        this.errors = new ArrayList<Throwable>();
    }
    
    @Override
    protected void verify() throws Throwable {
        MultipleFailureException.assertEmpty(this.errors);
    }
    
    public void addError(final Throwable error) {
        this.errors.add(error);
    }
    
    public <T> void checkThat(final T value, final Matcher<T> matcher) {
        this.checkThat("", value, matcher);
    }
    
    public <T> void checkThat(final String reason, final T value, final Matcher<T> matcher) {
        this.checkSucceeds((Callable<Object>)new Callable<Object>() {
            public Object call() throws Exception {
                Assert.assertThat(reason, value, matcher);
                return value;
            }
        });
    }
    
    public <T> T checkSucceeds(final Callable<T> callable) {
        try {
            return callable.call();
        }
        catch (Throwable e) {
            this.addError(e);
            return null;
        }
    }
}
