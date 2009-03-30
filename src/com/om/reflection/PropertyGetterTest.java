package com.om.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;

public class PropertyGetterTest {
    private final int testValue = 41;

    @SuppressWarnings("unused")
    private int getTestValuePrivate() {
        return testValue * 2;
    }

    public int getTestValueThrowsException() {
        throw new RuntimeException();
    }

    public int getTestValue() {
        return testValue;
    }

    @Test
    public void CanGetPropertyWithValidGetterMethod() throws Exception {
        Method m = getClass().getMethod("getTestValue", (Class[]) null);

        PropertyGetter pg = new PropertyGetter(null, m);
        assertEquals(41, pg.getValue(this));
    }

    @Test(expected = UnableToExecuteGetMethoOnBeanException.class)
    public void WrapsExceptionIfGetMethodNull() {
        PropertyGetter pg = new PropertyGetter(null, null);
        pg.getValue(this);
    }

    @Test(expected = UnableToExecuteGetMethoOnBeanException.class)
    public void WrapsExceptoinIfGetMethodNotPublic() throws Exception {
        Method m = getClass().getDeclaredMethod("getTestValuePrivate", (Class[]) null);
        PropertyGetter pg = new PropertyGetter(null, m);
        pg.getValue(this);
    }

    @Test(expected = UnableToExecuteGetMethoOnBeanException.class)
    public void WrapsExceptoinIfGetMethodThrowsException() throws Exception {
        Method m = getClass().getDeclaredMethod("getTestValueThrowsException", (Class[]) null);
        PropertyGetter pg = new PropertyGetter(null, m);
        pg.getValue(this);
    }

    @Test
    public void NamedMatchesWithStringsOfEqualValue() {
        PropertyGetter pg = new PropertyGetter("name", null);
        assertTrue(pg.named(new String("name")));
    }

    @Test
    public void TwoPropertyGettersWithSameValuesHaveSameHashCode() throws Exception {
        Method m = getClass().getDeclaredMethod("getTestValuePrivate", (Class[]) null);
        PropertyGetter pg1 = new PropertyGetter("name", m);
        PropertyGetter pg2 = new PropertyGetter(new String("name"), m);
        assertEquals(pg1.hashCode(), pg2.hashCode());
    }

    @Test
    public void TwoPropertyGettersWithSamValuesAreEqual() throws Exception {
        Method m = getClass().getDeclaredMethod("getTestValuePrivate", (Class[]) null);
        PropertyGetter pg1 = new PropertyGetter("name", m);
        PropertyGetter pg2 = new PropertyGetter(new String("name"), m);
        assertEquals(pg1, pg2);
    }

    @Test
    public void PropertyGetterNotEqualToObjectOfOtherType() {
        PropertyGetter pg = new PropertyGetter(null, null);
        assertFalse(pg.equals(this));
    }

    @Test
    public void PropertyGettersThatVaryInProperyNameNotConsideredEqual() throws Exception {
        Method m = getClass().getDeclaredMethod("getTestValuePrivate", (Class[]) null);
        PropertyGetter pg1 = new PropertyGetter("name", m);
        PropertyGetter pg2 = new PropertyGetter("eman", m);
        assertFalse(pg1.equals(pg2));
    }
}
