package com.ch.fastdfsapi.utils;

import org.springframework.stereotype.Component;

/**
 * 线程存储容器
 *
 * @author ch
 */
@Component
public class ThreadLocalUtil {

    private ThreadLocal<String> container;

    public ThreadLocalUtil() {
        this.container = new ThreadLocal<>();
    }

    /**
     * 获取 thread 存储的值
     */
    public String get() {
        return container.get();
    }

    /**
     * 设置 thread 存储数据
     */
    public void set(String data) {
        container.set(data);
    }

    /**
     * 清空 thread 值
     */
    public void remove() {
        container.remove();
    }


}
