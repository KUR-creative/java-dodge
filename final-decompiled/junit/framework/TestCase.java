// 
// Decompiled by Procyon v0.5.36
// 

package junit.framework;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public abstract class TestCase extends Assert implements Test
{
    private String fName;
    
    public TestCase() {
        this.fName = null;
    }
    
    public TestCase(final String name) {
        this.fName = name;
    }
    
    public int countTestCases() {
        return 1;
    }
    
    protected TestResult createResult() {
        return new TestResult();
    }
    
    public TestResult run() {
        final TestResult result = this.createResult();
        this.run(result);
        return result;
    }
    
    public void run(final TestResult result) {
        result.run(this);
    }
    
    public void runBare() throws Throwable {
        Throwable exception = null;
        this.setUp();
        try {
            this.runTest();
        }
        catch (Throwable running) {
            exception = running;
            try {
                this.tearDown();
            }
            catch (Throwable tearingDown) {
                if (exception == null) {
                    exception = tearingDown;
                }
            }
        }
        finally {
            try {
                this.tearDown();
            }
            catch (Throwable tearingDown2) {
                if (exception == null) {
                    exception = tearingDown2;
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
    }
    
    protected void runTest() throws Throwable {
        assertNotNull("TestCase.fName cannot be null", this.fName);
        Method runMethod = null;
        try {
            runMethod = this.getClass().getMethod(this.fName, (Class<?>[])null);
        }
        catch (NoSuchMethodException e3) {
            fail("Method \"" + this.fName + "\" not found");
        }
        if (!Modifier.isPublic(runMethod.getModifiers())) {
            fail("Method \"" + this.fName + "\" should be public");
        }
        try {
            runMethod.invoke(this, new Object[0]);
        }
        catch (InvocationTargetException e) {
            e.fillInStackTrace();
            throw e.getTargetException();
        }
        catch (IllegalAccessException e2) {
            e2.fillInStackTrace();
            throw e2;
        }
    }
    
    public static void assertTrue(final String message, final boolean condition) {
        Assert.assertTrue(message, condition);
    }
    
    public static void assertTrue(final boolean condition) {
        Assert.assertTrue(condition);
    }
    
    public static void assertFalse(final String message, final boolean condition) {
        Assert.assertFalse(message, condition);
    }
    
    public static void assertFalse(final boolean condition) {
        Assert.assertFalse(condition);
    }
    
    public static void fail(final String message) {
        Assert.fail(message);
    }
    
    public static void fail() {
        Assert.fail();
    }
    
    public static void assertEquals(final String message, final Object expected, final Object actual) {
        Assert.assertEquals(message, expected, actual);
    }
    
    public static void assertEquals(final Object expected, final Object actual) {
        Assert.assertEquals(expected, actual);
    }
    
    public static void assertEquals(final String message, final String expected, final String actual) {
        Assert.assertEquals(message, expected, actual);
    }
    
    public static void assertEquals(final String expected, final String actual) {
        Assert.assertEquals(expected, actual);
    }
    
    public static void assertEquals(final String message, final double expected, final double actual, final double delta) {
        Assert.assertEquals(message, expected, actual, delta);
    }
    
    public static void assertEquals(final double expected, final double actual, final double delta) {
        Assert.assertEquals(expected, actual, delta);
    }
    
    public static void assertEquals(final String message, final float expected, final float actual, final float delta) {
        Assert.assertEquals(message, expected, actual, delta);
    }
    
    public static void assertEquals(final float expected, final float actual, final float delta) {
        Assert.assertEquals(expected, actual, delta);
    }
    
    public static void assertEquals(final String message, final long expected, final long actual) {
        Assert.assertEquals(message, expected, actual);
    }
    
    public static void assertEquals(final long expected, final long actual) {
        Assert.assertEquals(expected, actual);
    }
    
    public static void assertEquals(final String message, final boolean expected, final boolean actual) {
        Assert.assertEquals(message, expected, actual);
    }
    
    public static void assertEquals(final boolean expected, final boolean actual) {
        Assert.assertEquals(expected, actual);
    }
    
    public static void assertEquals(final String message, final byte expected, final byte actual) {
        Assert.assertEquals(message, expected, actual);
    }
    
    public static void assertEquals(final byte expected, final byte actual) {
        Assert.assertEquals(expected, actual);
    }
    
    public static void assertEquals(final String message, final char expected, final char actual) {
        Assert.assertEquals(message, expected, actual);
    }
    
    public static void assertEquals(final char expected, final char actual) {
        Assert.assertEquals(expected, actual);
    }
    
    public static void assertEquals(final String message, final short expected, final short actual) {
        Assert.assertEquals(message, expected, actual);
    }
    
    public static void assertEquals(final short expected, final short actual) {
        Assert.assertEquals(expected, actual);
    }
    
    public static void assertEquals(final String message, final int expected, final int actual) {
        Assert.assertEquals(message, expected, actual);
    }
    
    public static void assertEquals(final int expected, final int actual) {
        Assert.assertEquals(expected, actual);
    }
    
    public static void assertNotNull(final Object object) {
        Assert.assertNotNull(object);
    }
    
    public static void assertNotNull(final String message, final Object object) {
        Assert.assertNotNull(message, object);
    }
    
    public static void assertNull(final Object object) {
        Assert.assertNull(object);
    }
    
    public static void assertNull(final String message, final Object object) {
        Assert.assertNull(message, object);
    }
    
    public static void assertSame(final String message, final Object expected, final Object actual) {
        Assert.assertSame(message, expected, actual);
    }
    
    public static void assertSame(final Object expected, final Object actual) {
        Assert.assertSame(expected, actual);
    }
    
    public static void assertNotSame(final String message, final Object expected, final Object actual) {
        Assert.assertNotSame(message, expected, actual);
    }
    
    public static void assertNotSame(final Object expected, final Object actual) {
        Assert.assertNotSame(expected, actual);
    }
    
    public static void failSame(final String message) {
        Assert.failSame(message);
    }
    
    public static void failNotSame(final String message, final Object expected, final Object actual) {
        Assert.failNotSame(message, expected, actual);
    }
    
    public static void failNotEquals(final String message, final Object expected, final Object actual) {
        Assert.failNotEquals(message, expected, actual);
    }
    
    public static String format(final String message, final Object expected, final Object actual) {
        return Assert.format(message, expected, actual);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    @Override
    public String toString() {
        return this.getName() + "(" + this.getClass().getName() + ")";
    }
    
    public String getName() {
        return this.fName;
    }
    
    public void setName(final String name) {
        this.fName = name;
    }
}
