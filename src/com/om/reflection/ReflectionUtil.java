package com.om.reflection;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ReflectionUtil {
    static final Map<Class<?>, ObjectPropertyGetters> cache = new ConcurrentHashMap<Class<?>, ObjectPropertyGetters>();

    public static void validateAllPropertiesExistIn(String[] propertiesToTranslate, Class<?> clazz) {
        ObjectPropertyGetters opg = getPropertyGettersFor(clazz);

        for (String propertyName : propertiesToTranslate)
            validateIndividualProperty(clazz, opg, propertyName);
    }

    private static void validateIndividualProperty(Class<?> clazz, ObjectPropertyGetters opg, String propertyName) {
        if (opg.getterNamed(propertyName) == null)
            throw new PropertyDoesNotExistInBeanException(propertyName, clazz);
    }

    public static ObjectPropertyGetters getPropertyGettersFor(Class<?> clazz) {
        ObjectPropertyGetters opg = cache.get(clazz);

        if (opg != null)
            return opg;

        opg = findBeanGetterMethodsFor(clazz);

        cache.put(clazz, opg);

        return opg;
    }

    private static ObjectPropertyGetters findBeanGetterMethodsFor(Class<?> clazz) {
        ObjectPropertyGetters propertGetters = new ObjectPropertyGetters();

        for (Method m : clazz.getMethods())
            if (conformsToGetBeanMethodSigniture(m))
                addGetMethod(propertGetters, m);
            else if (confirmsToIsBeanMethodSigniture(m))
                addIsMethod(propertGetters, m);

        return propertGetters;
    }

    private static void addIsMethod(ObjectPropertyGetters propertGetters, Method m) {
        String propertyName = lowercase(m.getName().substring(2));
        addNewPropertyDescriptor(propertGetters, m, propertyName);
   }

    private static void addNewPropertyDescriptor(ObjectPropertyGetters propertGetters, Method m,
            String propertyName) {
        PropertyGetter pd = new PropertyGetter(propertyName, m);
        propertGetters.add(pd);
    }

    private static boolean confirmsToIsBeanMethodSigniture(Method m) {
        return m.getName().startsWith("is") && m.getParameterTypes().length == 0
                && (m.getReturnType() == Boolean.class || m.getReturnType() == Boolean.TYPE);
    }

    private static void addGetMethod(ObjectPropertyGetters propertGetters, Method m) {
        String propertyName = lowercase(m.getName().substring(3));
        addNewPropertyDescriptor(propertGetters, m, propertyName);
    }

    private static boolean conformsToGetBeanMethodSigniture(Method m) {
        return m.getName().startsWith("get") && m.getParameterTypes().length == 0 && m.getReturnType() != Void.TYPE
                && m.getReturnType() != Void.class;
    }

    private static String lowercase(String string) {
        return Character.toLowerCase(string.charAt(0)) + string.substring(1);
    }

    public static PropertyGetter getPropertyGetterNamed(Class<?> clazz, String propertyName) {
        for (PropertyGetter pg : getPropertyGettersFor(clazz))
            if (pg.named(propertyName))
                return pg;

        throw new PropertyDoesNotExistInBeanException(propertyName, clazz);
    }

    public static void clearCache() {
        cache.clear();
    }
}
