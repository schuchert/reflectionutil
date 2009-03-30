package com.om.reflection;

public class PropertyDoesNotExistInBeanException extends RuntimeException {
    public final String propertyName;
    public final Class<?> clazz;

    public PropertyDoesNotExistInBeanException(String propertyName, Class<?> clazz) {
        super(String.format("Propery: %s, does not exist in: %s", propertyName, clazz.getName()));

        this.propertyName = propertyName;
        this.clazz = clazz;
    }

    private static final long serialVersionUID = 1L;
}
