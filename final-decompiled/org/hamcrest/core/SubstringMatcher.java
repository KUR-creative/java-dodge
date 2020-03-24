// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public abstract class SubstringMatcher extends TypeSafeMatcher<String>
{
    protected final String substring;
    
    protected SubstringMatcher(final String substring) {
        this.substring = substring;
    }
    
    public boolean matchesSafely(final String item) {
        return this.evalSubstringOf(item);
    }
    
    public void describeMismatchSafely(final String item, final Description mismatchDescription) {
        mismatchDescription.appendText("was \"").appendText(item).appendText("\"");
    }
    
    public void describeTo(final Description description) {
        description.appendText("a string ").appendText(this.relationship()).appendText(" ").appendValue(this.substring);
    }
    
    protected abstract boolean evalSubstringOf(final String p0);
    
    protected abstract String relationship();
}
