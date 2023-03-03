// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.validator;

import java.util.List;
import org.junit.runners.model.TestClass;

public interface TestClassValidator
{
    List<Exception> validateTestClass(final TestClass p0);
}
