package com.ch.fastdfsapi.utils;

import org.springframework.beans.BeanUtils;

/**
 * 提供 bean 的属性 Copy
 *
 * @author ch
 */
public class BeanUtil {


    private BeanUtil() {
    }

    /**
     * 复制出一个新的对象
     * <p>
     * 未经测试慎用
     *
     * @param source 原数据
     * @return 一个新的对象
     */
    public static <T> T copy(T source) {
        if (source == null) {
            return null;
        }

        Class<?> clazz = source.getClass();
        Object instance;

        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        T dest = (T) instance;
        BeanUtils.copyProperties(source, dest);
        return dest;
    }

    /**
     * 方便 Coding 而对原先的 {@link BeanUtils}.copyProperties() 做出一些调整
     *
     * @param source 原数据
     * @param clazz  复制对象的 class
     * @return 类型为 param2 的新对象
     */
    public static <T, E> E copyProperties(T source, Class<E> clazz) {
        if (source == null || clazz == null) {
            return null;
        }
        E instance;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        BeanUtils.copyProperties(source, instance);
        return instance;
    }
}
