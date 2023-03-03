// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners.model;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;

public abstract class FrameworkMember<T extends FrameworkMember<T>> implements Annotatable
{
    abstract boolean isShadowedBy(final T p0);
    
    boolean isShadowedBy(final List<T> members) {
        for (final T each : members) {
            if (this.isShadowedBy(each)) {
                return true;
            }
        }
        return false;
    }
    
    protected abstract int getModifiers();
    
    public boolean isStatic() {
        return Modifier.isStatic(this.getModifiers());
    }
    
    public boolean isPublic() {
        return Modifier.isPublic(this.getModifiers());
    }
    
    public abstract String getName();
    
    public abstract Class<?> getType();
    
    public abstract Class<?> getDeclaringClass();
}
