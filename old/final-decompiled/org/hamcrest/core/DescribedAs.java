// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.Description;
import java.util.regex.Pattern;
import org.hamcrest.Matcher;
import org.hamcrest.BaseMatcher;

public class DescribedAs<T> extends BaseMatcher<T>
{
    private final String descriptionTemplate;
    private final Matcher<T> matcher;
    private final Object[] values;
    private static final Pattern ARG_PATTERN;
    
    public DescribedAs(final String descriptionTemplate, final Matcher<T> matcher, final Object[] values) {
        this.descriptionTemplate = descriptionTemplate;
        this.matcher = matcher;
        this.values = values.clone();
    }
    
    public boolean matches(final Object o) {
        return this.matcher.matches(o);
    }
    
    public void describeTo(final Description description) {
        final java.util.regex.Matcher arg = DescribedAs.ARG_PATTERN.matcher(this.descriptionTemplate);
        int textStart = 0;
        while (arg.find()) {
            description.appendText(this.descriptionTemplate.substring(textStart, arg.start()));
            description.appendValue(this.values[Integer.parseInt(arg.group(1))]);
            textStart = arg.end();
        }
        if (textStart < this.descriptionTemplate.length()) {
            description.appendText(this.descriptionTemplate.substring(textStart));
        }
    }
    
    @Override
    public void describeMismatch(final Object item, final Description description) {
        this.matcher.describeMismatch(item, description);
    }
    
    @Factory
    public static <T> Matcher<T> describedAs(final String description, final Matcher<T> matcher, final Object... values) {
        return new DescribedAs<T>(description, matcher, values);
    }
    
    static {
        ARG_PATTERN = Pattern.compile("%([0-9]+)");
    }
}
