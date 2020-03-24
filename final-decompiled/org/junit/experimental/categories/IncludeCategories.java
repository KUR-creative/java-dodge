// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.categories;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import org.junit.runner.manipulation.Filter;
import java.util.List;

public final class IncludeCategories extends CategoryFilterFactory
{
    @Override
    protected Filter createFilter(final List<Class<?>> categories) {
        return new IncludesAny(categories);
    }
    
    private static class IncludesAny extends Categories.CategoryFilter
    {
        public IncludesAny(final List<Class<?>> categories) {
            this(new HashSet<Class<?>>(categories));
        }
        
        public IncludesAny(final Set<Class<?>> categories) {
            super(true, categories, true, null);
        }
        
        @Override
        public String describe() {
            return "includes " + super.describe();
        }
    }
}
