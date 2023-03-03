// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.theories;

import org.junit.experimental.theories.internal.SpecificDataPointsSupplier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
@ParametersSuppliedBy(SpecificDataPointsSupplier.class)
public @interface FromDataPoints {
    String value();
}
