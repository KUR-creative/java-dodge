// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.theories.suppliers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import org.junit.experimental.theories.ParametersSuppliedBy;
import java.lang.annotation.Annotation;

@ParametersSuppliedBy(TestedOnSupplier.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface TestedOn {
    int[] ints();
}
