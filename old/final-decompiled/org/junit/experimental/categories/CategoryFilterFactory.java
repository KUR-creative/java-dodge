// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.categories;

import org.junit.internal.Classes;
import java.util.ArrayList;
import java.util.List;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.FilterFactoryParams;
import org.junit.runner.FilterFactory;

abstract class CategoryFilterFactory implements FilterFactory
{
    public Filter createFilter(final FilterFactoryParams params) throws FilterNotCreatedException {
        try {
            return this.createFilter(this.parseCategories(params.getArgs()));
        }
        catch (ClassNotFoundException e) {
            throw new FilterNotCreatedException(e);
        }
    }
    
    protected abstract Filter createFilter(final List<Class<?>> p0);
    
    private List<Class<?>> parseCategories(final String categories) throws ClassNotFoundException {
        final List<Class<?>> categoryClasses = new ArrayList<Class<?>>();
        for (final String category : categories.split(",")) {
            final Class<?> categoryClass = Classes.getClass(category);
            categoryClasses.add(categoryClass);
        }
        return categoryClasses;
    }
}
