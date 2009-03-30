package com.om.reflection;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ObjectPropertyGetters implements Iterable<PropertyGetter> {
    private final List<PropertyGetter> propertyGetters = new LinkedList<PropertyGetter>();

    @Override
    public Iterator<PropertyGetter> iterator() {
        return propertyGetters.iterator();
    }

    public void add(PropertyGetter propertyGetter) {
        propertyGetters.add(propertyGetter);
    }

    public int size() {
        return propertyGetters.size();
    }

    public PropertyGetter getterNamed(String propertyName) {
        for (PropertyGetter getter : this)
            if (getter.named(propertyName))
                return getter;

        return null;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");

        boolean firstAppended = false;

        for (PropertyGetter pg : this) {
            if (!firstAppended)
                firstAppended = true;
            else
                buffer.append(", ");
            buffer.append(pg);
        }
        buffer.append("]");

        return buffer.toString();
    }
}
