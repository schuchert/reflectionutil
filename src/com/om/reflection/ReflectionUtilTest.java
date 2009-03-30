package com.om.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;


public class ReflectionUtilTest {

    @Test
    public void CanGetPropertiesFromComputedGetters() {
        ObjectPropertyGetters getters = ReflectionUtil.getPropertyGettersFor(ClassWithComputedGetters.class);
        assertEquals(3, getters.size());
        assertNotNull(getters.getterNamed("class"));
        assertNotNull(getters.getterNamed("x"));
        assertNotNull(getters.getterNamed("y"));
    }

    private void validateHasOnlyClassProperty(Object obj) {
        ObjectPropertyGetters getters = ReflectionUtil.getPropertyGettersFor(obj.getClass());
        assertEquals(1, getters.size());
        assertNotNull(getters.getterNamed("class"));
    }

    @Test
    public void DoesNotFindGetMethodsReturningVoid() {
        @SuppressWarnings("unused")
        Object objWithVoidReturns = new Object() {
            public void getZ() {
            }

            public Void getE() {
                return null;
            }
        };

        validateHasOnlyClassProperty(objWithVoidReturns);
    }

    @Test
    public void DoesNotFindGetMethodsTakingParameters() {
        @SuppressWarnings("unused")
        Object objWithBadGetMethodTakingParams = new Object() {
            public String getZ(int x) {
                return null;
            }
        };

        validateHasOnlyClassProperty(objWithBadGetMethodTakingParams);
    }

    @Test
    public void FindsMethodsStartingWithIsReturningBoolean() {
        Object objWithValidIsMethod = new Object() {
            @SuppressWarnings("unused")
            public Boolean isX() {
                return true;
            }
        };

        ObjectPropertyGetters getters = ReflectionUtil.getPropertyGettersFor(objWithValidIsMethod.getClass());
        assertEquals(2, getters.size());
        assertNotNull(getters.getterNamed("x"));
    }

    @Test
    public void FindsMethodsStartingWithIsReturningBooleanPrimative() {
        Object objWithValidIsMethod = new Object() {
            @SuppressWarnings("unused")
            public boolean isX() {
                return true;
            }
        };

        ObjectPropertyGetters getters = ReflectionUtil.getPropertyGettersFor(objWithValidIsMethod.getClass());
        assertEquals(2, getters.size());
        assertNotNull(getters.getterNamed("x"));
    }

    @Test
    public void SkipsIsMethodReturningNonBoolean() {
        Object objWithBogusIsMethod = new Object() {
            @SuppressWarnings("unused")
            public int isX() {
                return 0;
            }
        };

        validateHasOnlyClassProperty(objWithBogusIsMethod);
    }

    @Test
    public void SkipsIsMethodTakingParameters() {
        Object objWithBogusIsMethod = new Object() {
            @SuppressWarnings("unused")
            public boolean isX(int x) {
                return false;
            }
        };

        validateHasOnlyClassProperty(objWithBogusIsMethod);
    }

    @Test
    public void DoesNotDoubleRegisterRegularProperties() {
        Object objectWithRegularGetter = new Object() {
            private int foo;

            @SuppressWarnings("unused")
            public int getFoo() {
                return foo;
            }
        };

        ObjectPropertyGetters getters = ReflectionUtil.getPropertyGettersFor(objectWithRegularGetter.getClass());
        assertEquals(2, getters.size());
    }

    @Test
    public void CachesResultsFromSameClass() {
        ObjectPropertyGetters firstResult = ReflectionUtil.getPropertyGettersFor(getClass());
        ObjectPropertyGetters secondResult = ReflectionUtil.getPropertyGettersFor(getClass());
        assertSame(firstResult, secondResult);
    }

    @Test
    public void CacheClearedWhenRequested() {
        ObjectPropertyGetters firstResult = ReflectionUtil.getPropertyGettersFor(getClass());
        ReflectionUtil.clearCache();
        ObjectPropertyGetters secondResult = ReflectionUtil.getPropertyGettersFor(getClass());
        assertNotSame(firstResult, secondResult);
    }
    
    @Test(expected = PropertyDoesNotExistInBeanException.class)
    public void exceptionThrowWhenMissingPropertyValidated() {
        ReflectionUtil.validateAllPropertiesExistIn(new String[] { "gggg" }, getClass());
    }
    
    @Test
    public void noExceptionThrowWhenValidatingLegitimateProperties() {
        ReflectionUtil.validateAllPropertiesExistIn(new String[] { "x", "y" }, ClassWithComputedGetters.class);
    }
   
    
    public static class ClassWithComputedGetters {
        public String getX() {
            return "x";
        }

        public int getY() {
            return 42;
        }
    }
}
