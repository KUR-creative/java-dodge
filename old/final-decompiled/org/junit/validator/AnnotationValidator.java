// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.validator;

import java.util.Collections;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.TestClass;
import java.util.List;

public abstract class AnnotationValidator
{
    private static final List<Exception> NO_VALIDATION_ERRORS;
    
    public List<Exception> validateAnnotatedClass(final TestClass testClass) {
        return AnnotationValidator.NO_VALIDATION_ERRORS;
    }
    
    public List<Exception> validateAnnotatedField(final FrameworkField field) {
        return AnnotationValidator.NO_VALIDATION_ERRORS;
    }
    
    public List<Exception> validateAnnotatedMethod(final FrameworkMethod method) {
        return AnnotationValidator.NO_VALIDATION_ERRORS;
    }
    
    static {
        NO_VALIDATION_ERRORS = Collections.emptyList();
    }
}
