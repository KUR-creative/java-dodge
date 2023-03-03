// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest;

public class MatcherAssert
{
    public static <T> void assertThat(final T actual, final Matcher<? super T> matcher) {
        assertThat("", actual, matcher);
    }
    
    public static <T> void assertThat(final String reason, final T actual, final Matcher<? super T> matcher) {
        if (!matcher.matches(actual)) {
            final Description description = new StringDescription();
            description.appendText(reason).appendText("\nExpected: ").appendDescriptionOf(matcher).appendText("\n     but: ");
            matcher.describeMismatch(actual, description);
            throw new AssertionError((Object)description.toString());
        }
    }
    
    public static void assertThat(final String reason, final boolean assertion) {
        if (!assertion) {
            throw new AssertionError((Object)reason);
        }
    }
}
