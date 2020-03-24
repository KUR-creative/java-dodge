// 
// Decompiled by Procyon v0.5.36
// 

package org.hamcrest;

public interface Description
{
    public static final Description NONE = new NullDescription();
    
    Description appendText(final String p0);
    
    Description appendDescriptionOf(final SelfDescribing p0);
    
    Description appendValue(final Object p0);
    
     <T> Description appendValueList(final String p0, final String p1, final String p2, final T... p3);
    
     <T> Description appendValueList(final String p0, final String p1, final String p2, final Iterable<T> p3);
    
    Description appendList(final String p0, final String p1, final String p2, final Iterable<? extends SelfDescribing> p3);
    
    public static final class NullDescription implements Description
    {
        public Description appendDescriptionOf(final SelfDescribing value) {
            return this;
        }
        
        public Description appendList(final String start, final String separator, final String end, final Iterable<? extends SelfDescribing> values) {
            return this;
        }
        
        public Description appendText(final String text) {
            return this;
        }
        
        public Description appendValue(final Object value) {
            return this;
        }
        
        public <T> Description appendValueList(final String start, final String separator, final String end, final T... values) {
            return this;
        }
        
        public <T> Description appendValueList(final String start, final String separator, final String end, final Iterable<T> values) {
            return this;
        }
        
        @Override
        public String toString() {
            return "";
        }
    }
}
