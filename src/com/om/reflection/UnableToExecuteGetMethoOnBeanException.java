package com.om.reflection;

import java.lang.reflect.Method;

public class UnableToExecuteGetMethoOnBeanException extends RuntimeException {
    public final Object bean;
    public final Method getMethod;
    public final String propertyName;
    public final Exception e;

    public UnableToExecuteGetMethoOnBeanException(Object bean, Method getMethod, String propertyName, Exception e) {
        super(String.format("Unable to read property: %s using: %s on: %s.", propertyName, getMethod, bean), e);
        this.bean = bean;
        this.getMethod = getMethod;
        this.propertyName = propertyName;
        this.e = e;
    }

    private static final long serialVersionUID = 1L;

}
