package com.ch.fastdfsapi.services;

import com.ch.fastdfsapi.model.DFSFile;
import com.ch.fastdfsapi.utils.Msg;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 文件存储系统api接口
 */
public interface FastDFSApi {

    /**
     * 上传文件
     *
     * @param path 文件绝对路径
     */
    String upload(String path) throws IOException, MyException;

    /**
     * 上传文件
     *
     * @param data 字节数据
     */
    Msg<String> upload(byte[] data, String fileName) throws IOException, MyException;

    /**
     * 上传文件
     *
     * @param data 字节数据
     * @param meta 元数据
     */
    Msg<Object> upload(byte[] data, Map<String,String> meta) throws IOException, MyException;

    /**
     * 上传文件
     *
     * @param file 文件对象
     */
    DFSFile upload(DFSFile file) throws IOException, MyException;

    /**
     * 获取文件对象信息
     *
     * @param fileId 下载文件id
     */
    DFSFile getDFSFile(String fileId) throws IOException, MyException;

    /**
     * 下载文件
     *
     * @param file_name 下载文件名
     */
    File download(String file_name) throws IOException, MyException;

    /**
     * 下载文件-获取输入流
     *
     * @param fileId 下载文件id
     */
    InputStream downloadGetStream(String fileId) throws IOException, MyException;

    /**
     * 获取文件详细信息
     *
     * @param file_name 文件名
     */
    FileInfo getFileInfo(String file_name) throws IOException, MyException;

    /**
     * 获取文件详细信息
     *
     * @param fileId 文件id
     */
    NameValuePair[] getMeta(String fileId) throws IOException, MyException;

    /**
     * 删除文件
     *
     * @param fileId 文件id
     */
    boolean deleted(String fileId) throws IOException, MyException;

    /**
     * 设置元数据
     *
     * @param file 文件对象
     */
    boolean setMeta(DFSFile file) throws IOException, MyException;
}
