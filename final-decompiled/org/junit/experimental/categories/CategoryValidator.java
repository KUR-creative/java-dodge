// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.categories;

import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import org.junit.runners.model.FrameworkMethod;
import java.lang.annotation.Annotation;
import java.util.Set;
import org.junit.validator.AnnotationValidator;

public final class CategoryValidator extends AnnotationValidator
{
    private static final Set<Class<? extends Annotation>> INCOMPATIBLE_ANNOTATIONS;
    
    @Override
    public List<Exception> validateAnnotatedMethod(final FrameworkMethod method) {
        final List<Exception> errors = new ArrayList<Exception>();
        final Annotation[] arr$;
        final Annotation[] annotations = arr$ = method.getAnnotations();
        for (final Annotation annotation : arr$) {
            for (final Class<?> clazz : CategoryValidator.INCOMPATIBLE_ANNOTATIONS) {
                if (annotation.annotationType().isAssignableFrom(clazz)) {
                    this.addErrorMessage(errors, clazz);
                }
            }
        }
        return Collections.unmodifiableList((List<? extends Exception>)errors);
    }
    
    private void addErrorMessage(final List<Exception> errors, final Class<?> clazz) {
        final String message = String.format("@%s can not be combined with @Category", clazz.getSimpleName());
        errors.add(new Exception(message));
    }
    
    static {
        INCOMPATIBLE_ANNOTATIONS = Collections.unmodifiableSet((Set<? extends Class<? extends Annotation>>)new HashSet<Class<? extends Annotation>>((Collection<? extends Class<? extends Annotation>>)Arrays.asList(BeforeClass.class, AfterClass.class, Before.class, After.class)));
    }
}
