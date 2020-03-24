// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners.model;

import java.lang.annotation.Annotation;

public interface Annotatable
{
    Annotation[] getAnnotations();
    
     <T extends Annotation> T getAnnotation(final Class<T> p0);
}
