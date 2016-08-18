package ru.sbt.work;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheMemory implements CacheWork {

    Map<List<Object>, Object> map = new HashMap<>();


    @Override
    public void write(List<Object> key, Object obj) {
        map.put(key, obj);
    }

    @Override
    public Object read(List<Object> key) {
        return map.get(key);
    }
}
