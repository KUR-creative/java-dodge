// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners.parameterized;

import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.junit.runners.model.TestClass;

public class TestWithParameters
{
    private final String name;
    private final TestClass testClass;
    private final List<Object> parameters;
    
    public TestWithParameters(final String name, final TestClass testClass, final List<Object> parameters) {
        notNull(name, "The name is missing.");
        notNull(testClass, "The test class is missing.");
        notNull(parameters, "The parameters are missing.");
        this.name = name;
        this.testClass = testClass;
        this.parameters = Collections.unmodifiableList((List<?>)new ArrayList<Object>(parameters));
    }
    
    public String getName() {
        return this.name;
    }
    
    public TestClass getTestClass() {
        return this.testClass;
    }
    
    public List<Object> getParameters() {
        return this.parameters;
    }
    
    @Override
    public int hashCode() {
        final int prime = 14747;
        int result = prime + this.name.hashCode();
        result = prime * result + this.testClass.hashCode();
        return prime * result + this.parameters.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final TestWithParameters other = (TestWithParameters)obj;
        return this.name.equals(other.name) && this.parameters.equals(other.parameters) && this.testClass.equals(other.testClass);
    }
    
    @Override
    public String toString() {
        return this.testClass.getName() + " '" + this.name + "' with parameters " + this.parameters;
    }
    
    private static void notNull(final Object value, final String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
    }
}
