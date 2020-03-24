// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.categories;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import org.junit.runner.Description;
import java.util.Set;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.model.InitializationError;
import org.junit.runner.manipulation.Filter;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.Suite;

public class Categories extends Suite
{
    public Categories(final Class<?> klass, final RunnerBuilder builder) throws InitializationError {
        super(klass, builder);
        try {
            final Set<Class<?>> included = getIncludedCategory(klass);
            final Set<Class<?>> excluded = getExcludedCategory(klass);
            final boolean isAnyIncluded = isAnyIncluded(klass);
            final boolean isAnyExcluded = isAnyExcluded(klass);
            this.filter(CategoryFilter.categoryFilter(isAnyIncluded, included, isAnyExcluded, excluded));
        }
        catch (NoTestsRemainException e) {
            throw new InitializationError(e);
        }
        assertNoCategorizedDescendentsOfUncategorizeableParents(this.getDescription());
    }
    
    private static Set<Class<?>> getIncludedCategory(final Class<?> klass) {
        final IncludeCategory annotation = klass.getAnnotation(IncludeCategory.class);
        return createSet((Class<?>[])((annotation == null) ? null : annotation.value()));
    }
    
    private static boolean isAnyIncluded(final Class<?> klass) {
        final IncludeCategory annotation = klass.getAnnotation(IncludeCategory.class);
        return annotation == null || annotation.matchAny();
    }
    
    private static Set<Class<?>> getExcludedCategory(final Class<?> klass) {
        final ExcludeCategory annotation = klass.getAnnotation(ExcludeCategory.class);
        return createSet((Class<?>[])((annotation == null) ? null : annotation.value()));
    }
    
    private static boolean isAnyExcluded(final Class<?> klass) {
        final ExcludeCategory annotation = klass.getAnnotation(ExcludeCategory.class);
        return annotation == null || annotation.matchAny();
    }
    
    private static void assertNoCategorizedDescendentsOfUncategorizeableParents(final Description description) throws InitializationError {
        if (!canHaveCategorizedChildren(description)) {
            assertNoDescendantsHaveCategoryAnnotations(description);
        }
        for (final Description each : description.getChildren()) {
            assertNoCategorizedDescendentsOfUncategorizeableParents(each);
        }
    }
    
    private static void assertNoDescendantsHaveCategoryAnnotations(final Description description) throws InitializationError {
        for (final Description each : description.getChildren()) {
            if (each.getAnnotation(Category.class) != null) {
                throw new InitializationError("Category annotations on Parameterized classes are not supported on individual methods.");
            }
            assertNoDescendantsHaveCategoryAnnotations(each);
        }
    }
    
    private static boolean canHaveCategorizedChildren(final Description description) {
        for (final Description each : description.getChildren()) {
            if (each.getTestClass() == null) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean hasAssignableTo(final Set<Class<?>> assigns, final Class<?> to) {
        for (final Class<?> from : assigns) {
            if (to.isAssignableFrom(from)) {
                return true;
            }
        }
        return false;
    }
    
    private static Set<Class<?>> createSet(final Class<?>... t) {
        final Set<Class<?>> set = new HashSet<Class<?>>();
        if (t != null) {
            Collections.addAll(set, t);
        }
        return set;
    }
    
    public static class CategoryFilter extends Filter
    {
        private final Set<Class<?>> included;
        private final Set<Class<?>> excluded;
        private final boolean includedAny;
        private final boolean excludedAny;
        
        public static CategoryFilter include(final boolean matchAny, final Class<?>... categories) {
            if (hasNull(categories)) {
                throw new NullPointerException("has null category");
            }
            return categoryFilter(matchAny, createSet((Class<?>[])categories), true, null);
        }
        
        public static CategoryFilter include(final Class<?> category) {
            return include(true, category);
        }
        
        public static CategoryFilter include(final Class<?>... categories) {
            return include(true, categories);
        }
        
        public static CategoryFilter exclude(final boolean matchAny, final Class<?>... categories) {
            if (hasNull(categories)) {
                throw new NullPointerException("has null category");
            }
            return categoryFilter(true, null, matchAny, createSet((Class<?>[])categories));
        }
        
        public static CategoryFilter exclude(final Class<?> category) {
            return exclude(true, category);
        }
        
        public static CategoryFilter exclude(final Class<?>... categories) {
            return exclude(true, categories);
        }
        
        public static CategoryFilter categoryFilter(final boolean matchAnyInclusions, final Set<Class<?>> inclusions, final boolean matchAnyExclusions, final Set<Class<?>> exclusions) {
            return new CategoryFilter(matchAnyInclusions, inclusions, matchAnyExclusions, exclusions);
        }
        
        protected CategoryFilter(final boolean matchAnyIncludes, final Set<Class<?>> includes, final boolean matchAnyExcludes, final Set<Class<?>> excludes) {
            this.includedAny = matchAnyIncludes;
            this.excludedAny = matchAnyExcludes;
            this.included = copyAndRefine(includes);
            this.excluded = copyAndRefine(excludes);
        }
        
        @Override
        public String describe() {
            return this.toString();
        }
        
        @Override
        public String toString() {
            final StringBuilder description = new StringBuilder("categories ").append(this.included.isEmpty() ? "[all]" : this.included);
            if (!this.excluded.isEmpty()) {
                description.append(" - ").append(this.excluded);
            }
            return description.toString();
        }
        
        @Override
        public boolean shouldRun(final Description description) {
            if (this.hasCorrectCategoryAnnotation(description)) {
                return true;
            }
            for (final Description each : description.getChildren()) {
                if (this.shouldRun(each)) {
                    return true;
                }
            }
            return false;
        }
        
        private boolean hasCorrectCategoryAnnotation(final Description description) {
            final Set<Class<?>> childCategories = categories(description);
            if (childCategories.isEmpty()) {
                return this.included.isEmpty();
            }
            if (!this.excluded.isEmpty()) {
                if (this.excludedAny) {
                    if (this.matchesAnyParentCategories(childCategories, this.excluded)) {
                        return false;
                    }
                }
                else if (this.matchesAllParentCategories(childCategories, this.excluded)) {
                    return false;
                }
            }
            if (this.included.isEmpty()) {
                return true;
            }
            if (this.includedAny) {
                return this.matchesAnyParentCategories(childCategories, this.included);
            }
            return this.matchesAllParentCategories(childCategories, this.included);
        }
        
        private boolean matchesAnyParentCategories(final Set<Class<?>> childCategories, final Set<Class<?>> parentCategories) {
            for (final Class<?> parentCategory : parentCategories) {
                if (hasAssignableTo(childCategories, parentCategory)) {
                    return true;
                }
            }
            return false;
        }
        
        private boolean matchesAllParentCategories(final Set<Class<?>> childCategories, final Set<Class<?>> parentCategories) {
            for (final Class<?> parentCategory : parentCategories) {
                if (!hasAssignableTo(childCategories, parentCategory)) {
                    return false;
                }
            }
            return true;
        }
        
        private static Set<Class<?>> categories(final Description description) {
            final Set<Class<?>> categories = new HashSet<Class<?>>();
            Collections.addAll(categories, directCategories(description));
            Collections.addAll(categories, directCategories(parentDescription(description)));
            return categories;
        }
        
        private static Description parentDescription(final Description description) {
            final Class<?> testClass = description.getTestClass();
            return (testClass == null) ? null : Description.createSuiteDescription(testClass);
        }
        
        private static Class<?>[] directCategories(final Description description) {
            if (description == null) {
                return (Class<?>[])new Class[0];
            }
            final Category annotation = description.getAnnotation(Category.class);
            return (annotation == null) ? new Class[0] : annotation.value();
        }
        
        private static Set<Class<?>> copyAndRefine(final Set<Class<?>> classes) {
            final HashSet<Class<?>> c = new HashSet<Class<?>>();
            if (classes != null) {
                c.addAll((Collection<?>)classes);
            }
            c.remove(null);
            return c;
        }
        
        private static boolean hasNull(final Class<?>... classes) {
            if (classes == null) {
                return false;
            }
            for (final Class<?> clazz : classes) {
                if (clazz == null) {
                    return true;
                }
            }
            return false;
        }
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExcludeCategory {
        Class<?>[] value() default {};
        
        boolean matchAny() default true;
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface IncludeCategory {
        Class<?>[] value() default {};
        
        boolean matchAny() default true;
    }
}
