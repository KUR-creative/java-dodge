// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.validator;

import java.lang.annotation.Inherited;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ValidateWith {
    Class<? extends AnnotationValidator> value();
}
