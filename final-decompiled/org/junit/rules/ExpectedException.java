// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.rules;

import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.internal.matchers.ThrowableCauseMatcher;
import org.junit.internal.matchers.ThrowableMessageMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class ExpectedException implements TestRule
{
    private final ExpectedExceptionMatcherBuilder matcherBuilder;
    private String missingExceptionMessage;
    
    public static ExpectedException none() {
        return new ExpectedException();
    }
    
    private ExpectedException() {
        this.matcherBuilder = new ExpectedExceptionMatcherBuilder();
        this.missingExceptionMessage = "Expected test to throw %s";
    }
    
    @Deprecated
    public ExpectedException handleAssertionErrors() {
        return this;
    }
    
    @Deprecated
    public ExpectedException handleAssumptionViolatedExceptions() {
        return this;
    }
    
    public ExpectedException reportMissingExceptionWithMessage(final String message) {
        this.missingExceptionMessage = message;
        return this;
    }
    
    public Statement apply(final Statement base, final Description description) {
        return new ExpectedExceptionStatement(base);
    }
    
    public void expect(final Matcher<?> matcher) {
        this.matcherBuilder.add(matcher);
    }
    
    public void expect(final Class<? extends Throwable> type) {
        this.expect(CoreMatchers.instanceOf(type));
    }
    
    public void expectMessage(final String substring) {
        this.expectMessage(CoreMatchers.containsString(substring));
    }
    
    public void expectMessage(final Matcher<String> matcher) {
        this.expect(ThrowableMessageMatcher.hasMessage(matcher));
    }
    
    public void expectCause(final Matcher<? extends Throwable> expectedCause) {
        this.expect(ThrowableCauseMatcher.hasCause(expectedCause));
    }
    
    private void handleException(final Throwable e) throws Throwable {
        if (this.isAnyExceptionExpected()) {
            Assert.assertThat(e, this.matcherBuilder.build());
            return;
        }
        throw e;
    }
    
    private boolean isAnyExceptionExpected() {
        return this.matcherBuilder.expectsThrowable();
    }
    
    private void failDueToMissingException() throws AssertionError {
        Assert.fail(this.missingExceptionMessage());
    }
    
    private String missingExceptionMessage() {
        final String expectation = StringDescription.toString(this.matcherBuilder.build());
        return String.format(this.missingExceptionMessage, expectation);
    }
    
    private class ExpectedExceptionStatement extends Statement
    {
        private final Statement next;
        
        public ExpectedExceptionStatement(final Statement base) {
            this.next = base;
        }
        
        @Override
        public void evaluate() throws Throwable {
            try {
                this.next.evaluate();
            }
            catch (Throwable e) {
                ExpectedException.this.handleException(e);
                return;
            }
            if (ExpectedException.this.isAnyExceptionExpected()) {
                ExpectedException.this.failDueToMissingException();
            }
        }
    }
}
