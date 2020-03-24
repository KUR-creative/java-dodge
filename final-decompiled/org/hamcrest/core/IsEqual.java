// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import java.lang.reflect.Array;
import org.hamcrest.Description;
import org.hamcrest.BaseMatcher;

public class IsEqual<T> extends BaseMatcher<T>
{
    private final Object expectedValue;
    
    public IsEqual(final T equalArg) {
        this.expectedValue = equalArg;
    }
    
    public boolean matches(final Object actualValue) {
        return areEqual(actualValue, this.expectedValue);
    }
    
    public void describeTo(final Description description) {
        description.appendValue(this.expectedValue);
    }
    
    private static boolean areEqual(final Object actual, final Object expected) {
        if (actual == null) {
            return expected == null;
        }
        if (expected != null && isArray(actual)) {
            return isArray(expected) && areArraysEqual(actual, expected);
        }
        return actual.equals(expected);
    }
    
    private static boolean areArraysEqual(final Object actualArray, final Object expectedArray) {
        return areArrayLengthsEqual(actualArray, expectedArray) && areArrayElementsEqual(actualArray, expectedArray);
    }
    
    private static boolean areArrayLengthsEqual(final Object actualArray, final Object expectedArray) {
        return Array.getLength(actualArray) == Array.getLength(expectedArray);
    }
    
    private static boolean areArrayElementsEqual(final Object actualArray, final Object expectedArray) {
        for (int i = 0; i < Array.getLength(actualArray); ++i) {
            if (!areEqual(Array.get(actualArray, i), Array.get(expectedArray, i))) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean isArray(final Object o) {
        return o.getClass().isArray();
    }
    
    @Factory
    public static <T> Matcher<T> equalTo(final T operand) {
        return new IsEqual<T>(operand);
    }
}
