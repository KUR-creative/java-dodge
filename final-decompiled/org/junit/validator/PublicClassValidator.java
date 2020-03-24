// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.validator;

import java.util.Collections;
import org.junit.runners.model.TestClass;
import java.util.List;

public class PublicClassValidator implements TestClassValidator
{
    private static final List<Exception> NO_VALIDATION_ERRORS;
    
    public List<Exception> validateTestClass(final TestClass testClass) {
        if (testClass.isPublic()) {
            return PublicClassValidator.NO_VALIDATION_ERRORS;
        }
        return Collections.singletonList(new Exception("The class " + testClass.getName() + " is not public."));
    }
    
    static {
        NO_VALIDATION_ERRORS = Collections.emptyList();
    }
}
