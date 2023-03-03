// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.validator;

import java.util.concurrent.ConcurrentHashMap;

public class AnnotationValidatorFactory
{
    private static final ConcurrentHashMap<ValidateWith, AnnotationValidator> VALIDATORS_FOR_ANNOTATION_TYPES;
    
    public AnnotationValidator createAnnotationValidator(final ValidateWith validateWithAnnotation) {
        final AnnotationValidator validator = AnnotationValidatorFactory.VALIDATORS_FOR_ANNOTATION_TYPES.get(validateWithAnnotation);
        if (validator != null) {
            return validator;
        }
        final Class<? extends AnnotationValidator> clazz = validateWithAnnotation.value();
        if (clazz == null) {
            throw new IllegalArgumentException("Can't create validator, value is null in annotation " + validateWithAnnotation.getClass().getName());
        }
        try {
            final AnnotationValidator annotationValidator = (AnnotationValidator)clazz.newInstance();
            AnnotationValidatorFactory.VALIDATORS_FOR_ANNOTATION_TYPES.putIfAbsent(validateWithAnnotation, annotationValidator);
            return AnnotationValidatorFactory.VALIDATORS_FOR_ANNOTATION_TYPES.get(validateWithAnnotation);
        }
        catch (Exception e) {
            throw new RuntimeException("Exception received when creating AnnotationValidator class " + clazz.getName(), e);
        }
    }
    
    static {
        VALIDATORS_FOR_ANNOTATION_TYPES = new ConcurrentHashMap<ValidateWith, AnnotationValidator>();
    }
}
