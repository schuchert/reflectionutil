package com.om.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ObjectPropertyGettersTest {
    private ObjectPropertyGetters objectPropertyGetters;

    @Before
    public void init() {
        objectPropertyGetters = new ObjectPropertyGetters();
    }

    @Test
    public void Size0UponCreation() {
        assertEquals(0, objectPropertyGetters.size());
    }

    @Test
    public void CanFindAddedPropertyGetterByName() {
        objectPropertyGetters.add(new PropertyGetter("b", null));
        assertNotNull(objectPropertyGetters.getterNamed("b"));
    }

    @Test
    public void UnregisteredPropertyNameResultsInNull() {
        assertNull(objectPropertyGetters.getterNamed("unknown"));
    }

    @Test
    public void CanIterateOverEmpty() {
        for (@SuppressWarnings("unused")
        PropertyGetter pg : objectPropertyGetters)
            ;
    }

    @Test
    public void CanIterateOverAddedPropertGetters() {
        PropertyGetterSpy pg1 = new PropertyGetterSpy();
        PropertyGetterSpy pg2 = new PropertyGetterSpy();

        objectPropertyGetters.add(pg1);
        objectPropertyGetters.add(pg2);

        for (PropertyGetter pg : objectPropertyGetters)
            pg.getValue(null);
        
        assertTrue(pg1.getValueCalled);
        assertTrue(pg2.getValueCalled);
    }

    public static class PropertyGetterSpy extends PropertyGetter {
        boolean getValueCalled;

        public PropertyGetterSpy() {
            super(null, null);
        }

        @Override
        public Object getValue(Object bean) {
            return getValueCalled = true;
        }
    }
}
