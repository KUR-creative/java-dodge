// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.matchers;

import org.hamcrest.Factory;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class StacktracePrintingMatcher<T extends Throwable> extends TypeSafeMatcher<T>
{
    private final Matcher<T> throwableMatcher;
    
    public StacktracePrintingMatcher(final Matcher<T> throwableMatcher) {
        this.throwableMatcher = throwableMatcher;
    }
    
    public void describeTo(final Description description) {
        this.throwableMatcher.describeTo(description);
    }
    
    @Override
    protected boolean matchesSafely(final T item) {
        return this.throwableMatcher.matches(item);
    }
    
    @Override
    protected void describeMismatchSafely(final T item, final Description description) {
        this.throwableMatcher.describeMismatch(item, description);
        description.appendText("\nStacktrace was: ");
        description.appendText(this.readStacktrace(item));
    }
    
    private String readStacktrace(final Throwable throwable) {
        final StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
    
    @Factory
    public static <T extends Throwable> Matcher<T> isThrowable(final Matcher<T> throwableMatcher) {
        return new StacktracePrintingMatcher<T>(throwableMatcher);
    }
    
    @Factory
    public static <T extends Exception> Matcher<T> isException(final Matcher<T> exceptionMatcher) {
        return new StacktracePrintingMatcher<T>(exceptionMatcher);
    }
}
