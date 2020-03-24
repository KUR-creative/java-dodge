// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.experimental.theories;

public abstract class PotentialAssignment
{
    public static PotentialAssignment forValue(final String name, final Object value) {
        return new PotentialAssignment() {
            @Override
            public Object getValue() {
                return value;
            }
            
            @Override
            public String toString() {
                return String.format("[%s]", value);
            }
            
            @Override
            public String getDescription() {
                String valueString;
                if (value == null) {
                    valueString = "null";
                }
                else {
                    try {
                        valueString = String.format("\"%s\"", value);
                    }
                    catch (Throwable e) {
                        valueString = String.format("[toString() threw %s: %s]", e.getClass().getSimpleName(), e.getMessage());
                    }
                }
                return String.format("%s <from %s>", valueString, name);
            }
        };
    }
    
    public abstract Object getValue() throws CouldNotGenerateValueException;
    
    public abstract String getDescription() throws CouldNotGenerateValueException;
    
    public static class CouldNotGenerateValueException extends Exception
    {
        private static final long serialVersionUID = 1L;
        
        public CouldNotGenerateValueException() {
        }
        
        public CouldNotGenerateValueException(final Throwable e) {
            super(e);
        }
    }
}
