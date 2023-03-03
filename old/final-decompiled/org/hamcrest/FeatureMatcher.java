// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest;

import org.hamcrest.internal.ReflectiveTypeFinder;

public abstract class FeatureMatcher<T, U> extends TypeSafeDiagnosingMatcher<T>
{
    private static final ReflectiveTypeFinder TYPE_FINDER;
    private final Matcher<? super U> subMatcher;
    private final String featureDescription;
    private final String featureName;
    
    public FeatureMatcher(final Matcher<? super U> subMatcher, final String featureDescription, final String featureName) {
        super(FeatureMatcher.TYPE_FINDER);
        this.subMatcher = subMatcher;
        this.featureDescription = featureDescription;
        this.featureName = featureName;
    }
    
    protected abstract U featureValueOf(final T p0);
    
    @Override
    protected boolean matchesSafely(final T actual, final Description mismatch) {
        final U featureValue = this.featureValueOf(actual);
        if (!this.subMatcher.matches(featureValue)) {
            mismatch.appendText(this.featureName).appendText(" ");
            this.subMatcher.describeMismatch(featureValue, mismatch);
            return false;
        }
        return true;
    }
    
    public final void describeTo(final Description description) {
        description.appendText(this.featureDescription).appendText(" ").appendDescriptionOf(this.subMatcher);
    }
    
    static {
        TYPE_FINDER = new ReflectiveTypeFinder("featureValueOf", 1, 0);
    }
}
