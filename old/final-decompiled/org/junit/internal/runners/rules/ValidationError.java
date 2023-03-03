// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners.rules;

import java.lang.annotation.Annotation;
import org.junit.runners.model.FrameworkMember;

class ValidationError extends Exception
{
    public ValidationError(final FrameworkMember<?> member, final Class<? extends Annotation> annotation, final String suffix) {
        super(String.format("The @%s '%s' %s", annotation.getSimpleName(), member.getName(), suffix));
    }
}
