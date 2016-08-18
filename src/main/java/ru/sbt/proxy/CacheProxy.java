package ru.sbt.proxy;

import ru.sbt.annotation.Cache;
import ru.sbt.enums.CacheType;
import ru.sbt.work.CacheFile;
import ru.sbt.work.CacheMemory;
import ru.sbt.work.CacheWork;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CacheProxy implements InvocationHandler {

    private Object delegate;

    private CacheType cacheType = CacheType.IN_MEMORY;
    private boolean zip = false;
    private List<Class> identityBy;

    private Map<Integer, CacheWork> cacheObject = new HashMap<>(); //different cache for different method

    private String fullName;


    public CacheProxy(Object delegate, String name) {
        fullName = name != null ? name : "";
        this.delegate = delegate;
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.isAnnotationPresent(Cache.class)) return method.invoke(delegate, args);
        Cache cache = method.getAnnotation(Cache.class);
        fillField(cache, method.getParameterTypes());
        if (cacheObject == null || cacheObject.get(method.hashCode()) == null) {
            buildProxy(method.hashCode());
        }

        CacheWork cacheWork = cacheObject.get(method.hashCode());
        List<Object> key = calculateKey(args);

        Object obj = cacheWork.read(key);
        if (obj == null) {
            obj = invokeMethod(method, args);
            cacheWork.write(key, obj);

        } else {
            return obj;
        }
        return obj;
    }


    private List<Object> calculateKey(Object[] args) {
        List<Object> objects = new ArrayList<>();
        ListIterator<Class> iterator = identityBy.listIterator();
        for (int i = 0; i < args.length; i++) {
            while (iterator.hasNext()) {
                Class clazz = iterator.next();
                if (clazz == args[i].getClass()) {
                    objects.add(args[i]);
                    i++;
                } else {
                    i++;
                }
            }
        }
        return objects;
    }


    private Object invokeMethod(Method method, Object[] args) throws Throwable {
        Object obj;
        int sizeReturn = method.getAnnotation(Cache.class).listSize();
        try {
            obj = method.invoke(delegate, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error during invoke method");
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
        if (method.getReturnType() == List.class) {
            List list = (List) obj;
            obj = list.subList(0, sizeReturn == 0 ? list.size() : (sizeReturn > list.size() ? list.size() : sizeReturn));
        }
        return obj;
    }


    private void fillField(Cache cache, Class<?>[] parameterTypes) {
        cacheType = cache.cacheType();
        zip = cache.zip();
        String fileNamePrefix = cache.fileNamePrefix();
        fullName += fileNamePrefix;
        identityBy = cache.identityBy().length == 0 ? Arrays.asList(parameterTypes)
                : Arrays.asList(cache.identityBy()).subList(0, cache.identityBy().length);


    }


    private void buildProxy(Integer methodHash) throws IOException {
        if (cacheType == CacheType.IN_MEMORY) {
            cacheObject.put(methodHash, new CacheMemory());
        } else {
            cacheObject.put(methodHash, new CacheFile(String.valueOf(methodHash).substring(2, 5) + fullName, zip));
        }


    }
}
