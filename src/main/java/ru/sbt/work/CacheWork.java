package ru.sbt.work;

import java.util.List;

public interface CacheWork {


    void write(List<Object> key, Object obj);

    Object read(List<Object> key);


}
