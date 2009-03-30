package com.om.reflection;

import java.lang.reflect.Method;

public class PropertyGetter {
    public final String propertyName;
    public final Method getMethod;

    public PropertyGetter(String propertyName, Method getMethod) {
        this.propertyName = propertyName;
        this.getMethod = getMethod;
    }

    public <T> T getValue(Object bean, Class<T> clazz) {
        return clazz.cast(getValue(bean));
    }

    public Class<?> getPropertyType() {
        return getMethod.getReturnType();
    }

    public Object getValue(Object bean) {
        try {
            return getMethod.invoke(bean, (Object[]) null);
        } catch (Exception e) {
            throw new UnableToExecuteGetMethoOnBeanException(bean, getMethod, propertyName, e);
        }

    }

    public boolean named(String propertyName) {
        return this.propertyName.equals(propertyName);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof PropertyGetter) {
            PropertyGetter rhs = (PropertyGetter) other;
            return rhs.propertyName.equals(propertyName) && rhs.getMethod.equals(getMethod);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return propertyName.hashCode() % getMethod.hashCode();
    }

    @Override
    public String toString() {
        return String.format("[%s --> %s()]", propertyName, getMethod.getName());
    }
}
