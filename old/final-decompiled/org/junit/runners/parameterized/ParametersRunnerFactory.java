// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runners.parameterized;

import org.junit.runners.model.InitializationError;
import org.junit.runner.Runner;

public interface ParametersRunnerFactory
{
    Runner createRunnerForTestWithParameters(final TestWithParameters p0) throws InitializationError;
}
