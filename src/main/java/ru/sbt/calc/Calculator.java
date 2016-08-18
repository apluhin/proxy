package ru.sbt.calc;

import ru.sbt.annotation.Cache;
import ru.sbt.enums.CacheType;

import java.util.List;

public interface Calculator {

    @Cache(cacheType = CacheType.FILE, identityBy = {Integer.class, Integer.class}, zip = true, fileNamePrefix = "testData")
    int hardWord(Integer a, int b);

    @Cache(cacheType = CacheType.IN_MEMORY, identityBy = String.class)
    int hardWord(Integer a, int b, String c);


    @Cache(cacheType = CacheType.IN_MEMORY, identityBy = Integer.class, listSize = 2000)
    List<Integer> someList(int a);


}
