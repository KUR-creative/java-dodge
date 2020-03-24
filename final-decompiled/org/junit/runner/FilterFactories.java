// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runner;

import org.junit.internal.Classes;
import org.junit.runner.manipulation.Filter;

class FilterFactories
{
    public static Filter createFilterFromFilterSpec(final Request request, final String filterSpec) throws FilterFactory.FilterNotCreatedException {
        final Description topLevelDescription = request.getRunner().getDescription();
        String[] tuple;
        if (filterSpec.contains("=")) {
            tuple = filterSpec.split("=", 2);
        }
        else {
            tuple = new String[] { filterSpec, "" };
        }
        return createFilter(tuple[0], new FilterFactoryParams(topLevelDescription, tuple[1]));
    }
    
    public static Filter createFilter(final String filterFactoryFqcn, final FilterFactoryParams params) throws FilterFactory.FilterNotCreatedException {
        final FilterFactory filterFactory = createFilterFactory(filterFactoryFqcn);
        return filterFactory.createFilter(params);
    }
    
    public static Filter createFilter(final Class<? extends FilterFactory> filterFactoryClass, final FilterFactoryParams params) throws FilterFactory.FilterNotCreatedException {
        final FilterFactory filterFactory = createFilterFactory(filterFactoryClass);
        return filterFactory.createFilter(params);
    }
    
    static FilterFactory createFilterFactory(final String filterFactoryFqcn) throws FilterFactory.FilterNotCreatedException {
        Class<? extends FilterFactory> filterFactoryClass;
        try {
            filterFactoryClass = Classes.getClass(filterFactoryFqcn).asSubclass(FilterFactory.class);
        }
        catch (Exception e) {
            throw new FilterFactory.FilterNotCreatedException(e);
        }
        return createFilterFactory(filterFactoryClass);
    }
    
    static FilterFactory createFilterFactory(final Class<? extends FilterFactory> filterFactoryClass) throws FilterFactory.FilterNotCreatedException {
        try {
            return (FilterFactory)filterFactoryClass.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (Exception e) {
            throw new FilterFactory.FilterNotCreatedException(e);
        }
    }
}
