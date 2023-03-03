// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.theories;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface DataPoint {
    String[] value() default {};
    
    Class<? extends Throwable>[] ignoredExceptions() default {};
}
