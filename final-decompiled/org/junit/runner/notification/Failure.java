// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runner.notification;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.runner.Description;
import java.io.Serializable;

public class Failure implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final Description fDescription;
    private final Throwable fThrownException;
    
    public Failure(final Description description, final Throwable thrownException) {
        this.fThrownException = thrownException;
        this.fDescription = description;
    }
    
    public String getTestHeader() {
        return this.fDescription.getDisplayName();
    }
    
    public Description getDescription() {
        return this.fDescription;
    }
    
    public Throwable getException() {
        return this.fThrownException;
    }
    
    @Override
    public String toString() {
        return this.getTestHeader() + ": " + this.fThrownException.getMessage();
    }
    
    public String getTrace() {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);
        this.getException().printStackTrace(writer);
        return stringWriter.toString();
    }
    
    public String getMessage() {
        return this.getException().getMessage();
    }
}
