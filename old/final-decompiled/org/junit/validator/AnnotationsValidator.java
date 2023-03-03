// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.validator;

import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import java.util.Collections;
import java.lang.annotation.Annotation;
import org.junit.runners.model.Annotatable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import org.junit.runners.model.TestClass;
import java.util.List;

public final class AnnotationsValidator implements TestClassValidator
{
    private static final List<AnnotatableValidator<?>> VALIDATORS;
    
    public List<Exception> validateTestClass(final TestClass testClass) {
        final List<Exception> validationErrors = new ArrayList<Exception>();
        for (final AnnotatableValidator<?> validator : AnnotationsValidator.VALIDATORS) {
            final List<Exception> additionalErrors = validator.validateTestClass(testClass);
            validationErrors.addAll(additionalErrors);
        }
        return validationErrors;
    }
    
    static {
        VALIDATORS = Arrays.asList(new ClassValidator(), new MethodValidator(), new FieldValidator());
    }
    
    private abstract static class AnnotatableValidator<T extends Annotatable>
    {
        private static final AnnotationValidatorFactory ANNOTATION_VALIDATOR_FACTORY;
        
        abstract Iterable<T> getAnnotatablesForTestClass(final TestClass p0);
        
        abstract List<Exception> validateAnnotatable(final AnnotationValidator p0, final T p1);
        
        public List<Exception> validateTestClass(final TestClass testClass) {
            final List<Exception> validationErrors = new ArrayList<Exception>();
            for (final T annotatable : this.getAnnotatablesForTestClass(testClass)) {
                final List<Exception> additionalErrors = this.validateAnnotatable(annotatable);
                validationErrors.addAll(additionalErrors);
            }
            return validationErrors;
        }
        
        private List<Exception> validateAnnotatable(final T annotatable) {
            final List<Exception> validationErrors = new ArrayList<Exception>();
            for (final Annotation annotation : annotatable.getAnnotations()) {
                final Class<? extends Annotation> annotationType = annotation.annotationType();
                final ValidateWith validateWith = annotationType.getAnnotation(ValidateWith.class);
                if (validateWith != null) {
                    final AnnotationValidator annotationValidator = AnnotatableValidator.ANNOTATION_VALIDATOR_FACTORY.createAnnotationValidator(validateWith);
                    final List<Exception> errors = this.validateAnnotatable(annotationValidator, annotatable);
                    validationErrors.addAll(errors);
                }
            }
            return validationErrors;
        }
        
        static {
            ANNOTATION_VALIDATOR_FACTORY = new AnnotationValidatorFactory();
        }
    }
    
    private static class ClassValidator extends AnnotatableValidator<TestClass>
    {
        @Override
        Iterable<TestClass> getAnnotatablesForTestClass(final TestClass testClass) {
            return Collections.singletonList(testClass);
        }
        
        @Override
        List<Exception> validateAnnotatable(final AnnotationValidator validator, final TestClass testClass) {
            return validator.validateAnnotatedClass(testClass);
        }
    }
    
    private static class MethodValidator extends AnnotatableValidator<FrameworkMethod>
    {
        @Override
        Iterable<FrameworkMethod> getAnnotatablesForTestClass(final TestClass testClass) {
            return testClass.getAnnotatedMethods();
        }
        
        @Override
        List<Exception> validateAnnotatable(final AnnotationValidator validator, final FrameworkMethod method) {
            return validator.validateAnnotatedMethod(method);
        }
    }
    
    private static class FieldValidator extends AnnotatableValidator<FrameworkField>
    {
        @Override
        Iterable<FrameworkField> getAnnotatablesForTestClass(final TestClass testClass) {
            return testClass.getAnnotatedFields();
        }
        
        @Override
        List<Exception> validateAnnotatable(final AnnotationValidator validator, final FrameworkField field) {
            return validator.validateAnnotatedField(field);
        }
    }
}
