package com.om.reflection;

import java.lang.reflect.Field;

public class InvalidBeanMissingGetterException extends RuntimeException {
    public InvalidBeanMissingGetterException(Field field, Class<?> clazz) {
        super(String.format("Unable to find getter for field %s on class %s", field.getName(), clazz.getName()));
    }

    private static final long serialVersionUID = 1L;

}
