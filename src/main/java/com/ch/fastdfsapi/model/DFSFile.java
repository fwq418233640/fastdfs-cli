package com.ch.fastdfsapi.model;

import lombok.Data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 文件存储系统实体
 *
 * @author ch
 * @version 1.0
 */
@Data
public class DFSFile {

    /**
     * 文件对象id
     */
    private String fileId;
    /**
     * 文件对象
     */
    private File file;

    /**
     * 路径
     */
    private String path;
    /**
     * 字节信息
     */
    private Byte[] data;

    /**
     * 元数据
     */
    private Map<String, String> meta;

    private Boolean setMeta;


    public void push(String key, String val) {
        if (meta == null) {
            meta = new HashMap<String, String>();
        }
        meta.put(key, val);
    }


    public Set<Map.Entry<String, String>> getMeta() {
        if (meta == null) {
            return null;
        }
        return meta.entrySet();
    }
}
