// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.categories;

import org.junit.validator.ValidateWith;
import java.lang.annotation.Inherited;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ValidateWith(CategoryValidator.class)
public @interface Category {
    Class<?>[] value();
}
