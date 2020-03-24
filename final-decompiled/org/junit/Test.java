// 
// Decompiled by Procyon v0.5.36
// 

package org.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Test {
    Class<? extends Throwable> expected() default None.class;
    
    long timeout() default 0L;
    
    public static class None extends Throwable
    {
        private static final long serialVersionUID = 1L;
        
        private None() {
        }
    }
}
