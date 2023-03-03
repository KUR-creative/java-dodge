// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest;

public abstract class Condition<T>
{
    public static final NotMatched<Object> NOT_MATCHED;
    
    private Condition() {
    }
    
    public abstract boolean matching(final Matcher<T> p0, final String p1);
    
    public abstract <U> Condition<U> and(final Step<? super T, U> p0);
    
    public final boolean matching(final Matcher<T> match) {
        return this.matching(match, "");
    }
    
    public final <U> Condition<U> then(final Step<? super T, U> mapping) {
        return (Condition<U>)this.and((Step<? super T, Object>)mapping);
    }
    
    public static <T> Condition<T> notMatched() {
        return (Condition<T>)Condition.NOT_MATCHED;
    }
    
    public static <T> Condition<T> matched(final T theValue, final Description mismatch) {
        return new Matched<T>((Object)theValue, mismatch);
    }
    
    static {
        NOT_MATCHED = new NotMatched<Object>();
    }
    
    private static final class Matched<T> extends Condition<T>
    {
        private final T theValue;
        private final Description mismatch;
        
        private Matched(final T theValue, final Description mismatch) {
            super(null);
            this.theValue = theValue;
            this.mismatch = mismatch;
        }
        
        @Override
        public boolean matching(final Matcher<T> matcher, final String message) {
            if (matcher.matches(this.theValue)) {
                return true;
            }
            this.mismatch.appendText(message);
            matcher.describeMismatch(this.theValue, this.mismatch);
            return false;
        }
        
        @Override
        public <U> Condition<U> and(final Step<? super T, U> next) {
            return next.apply((Object)this.theValue, this.mismatch);
        }
    }
    
    private static final class NotMatched<T> extends Condition<T>
    {
        private NotMatched() {
            super(null);
        }
        
        @Override
        public boolean matching(final Matcher<T> match, final String message) {
            return false;
        }
        
        @Override
        public <U> Condition<U> and(final Step<? super T, U> mapping) {
            return Condition.notMatched();
        }
    }
    
    public interface Step<I, O>
    {
        Condition<O> apply(final I p0, final Description p1);
    }
}
