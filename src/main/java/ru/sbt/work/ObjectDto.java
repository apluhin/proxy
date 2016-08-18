package ru.sbt.work;

import java.io.Serializable;
import java.util.List;

public class ObjectDto implements Serializable {
    List<Object> key;
    Object object;

    public ObjectDto(List<Object> key, Object object) {
        this.key = key;
        this.object = object;
    }

    public List<Object> getKey() {
        return key;
    }

    public Object getObject() {
        return object;
    }
}
