package com.ch.fastdfsapi.services.impl;

import com.ch.fastdfsapi.model.DFSFile;
import com.ch.fastdfsapi.services.Client;
import com.ch.fastdfsapi.services.FastDFSApi;
import com.ch.fastdfsapi.utils.Msg;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 文件存储系统默认实现类
 */
@Service
public class FastServersImpl implements FastDFSApi {

    private TrackerClient tracker;

    @Autowired
    private Environment properties;

    /**
     * 上传文件
     *
     * @param path 文件绝对路径
     */
    @Override
    public String upload(String path) throws IOException, MyException {
        if (path == null) {
            return null;
        }

        TrackerServer trackerServer = this.getConnection();
        StorageClient1 client = new StorageClient1(trackerServer, null);

        NameValuePair[] metaList = new NameValuePair[1];
        metaList[0] = new NameValuePair("fileName", path);
        String fileId = client.upload_file1(path, null, metaList);
        trackerServer.close();
        return fileId;
    }


    /**
     * 上传文件
     *
     * @param data 字节数据
     */
    @Override
    public Msg<String> upload(byte[] data, String fileName) throws IOException, MyException {

        if (data == null || data.length < 1) {
            return Msg.fail("数据为空");
        }

        String file_ext_name = null;
        if (!StringUtils.isEmpty(fileName)) {
            String[] split = fileName.split("\\.");
            if (split.length == 2) {
                fileName = split[0];
                file_ext_name = split[1];
            }
        }

        TrackerServer trackerServer = this.getConnection();
        StorageClient1 client = new StorageClient1(trackerServer, null);
        NameValuePair[] metaList = new NameValuePair[1];
        metaList[0] = new NameValuePair("fileName", fileName);
        String[] fileIds = client.upload_file(data, file_ext_name, metaList);
        if (fileIds == null || fileIds.length == 0) {
            return Msg.fail();
        }

        String group = fileIds[0];
        String value = fileIds[1];
        trackerServer.close();
        return Msg.sucess(group + "/" + value);
    }

    @Override
    public Msg<Object> upload(byte[] data, Map<String, String> meta) throws IOException, MyException {

        if (data == null || data.length < 1) {
            return Msg.fail("数据为空");
        }

        if (meta == null || meta.size() == 0) {
            return Msg.fail("元数据不可为空 最少保留文件名称");
        }


        TrackerServer trackerServer = this.getConnection();
        StorageServer storeStorage = this.tracker.getStoreStorage(trackerServer, null);
        StorageClient1 client = new StorageClient1(trackerServer, storeStorage);

        final NameValuePair[] metaList = new NameValuePair[meta.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : meta.entrySet()) {
            metaList[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        String[] fileIds = client.upload_file(data, null, metaList);
        if (fileIds == null || fileIds.length == 0) {
            return Msg.fail("上传失败");
        }

        String group = fileIds[0];
        String value = fileIds[1];
        trackerServer.close();
        return Msg.sucess(group + "/" + value);
    }


    private TrackerServer getConnection() throws IOException {
        if (this.tracker == null) {
            Client global = new Client();
            try {
                global.init(properties);
                this.tracker = new TrackerClient(global.getG_tracker_group());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.tracker.getConnection();
    }

    /**
     * 上传文件
     *
     * @param file 文件对象
     */
    @Override
    public DFSFile upload(DFSFile file) throws IOException, MyException {
        if (file == null) {
            return null;
        }

        String path = file.getPath();
        NameValuePair[] metaList = new NameValuePair[1];
        metaList[0] = new NameValuePair("fileName", path);


        TrackerServer trackerServer = this.getConnection();
        StorageClient1 client = new StorageClient1(trackerServer, null);
        String fileId = client.upload_file1(path, null, metaList);

        //  设置元数据
        file.setFileId(fileId);
        boolean b = this.setMeta(file);
        file.setSetMeta(b);

        //  关闭链接
        trackerServer.close();
        return file;
    }

    /**
     * 获取文件对象信息
     *
     * @param fileId 下载文件id
     */
    @Override
    public DFSFile getDFSFile(String fileId) throws IOException, MyException {

        if (fileId == null) {
            return null;
        }

        String[] split = fileId.split("\\.");
        String suffix = "";
        if (split.length >= 2) {
            suffix = "." + split[1];
        }


        File file = new File(System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID().toString() + suffix);
        FileOutputStream stream = new FileOutputStream(file);

        byte[] bytes = this.downloadGetByteArray(fileId);
        if (bytes == null) {
            return null;
        }
        stream.write(bytes);
        stream.close();
        DFSFile dfsFile = new DFSFile();
        dfsFile.setFile(file);
        dfsFile.setFileId(fileId);
        NameValuePair[] meta = this.getMeta(fileId);
        if (meta != null) {
            for (NameValuePair nameValuePair : meta) {
                dfsFile.push(nameValuePair.getName(), nameValuePair.getValue());
            }
        }
        return dfsFile;
    }

    /**
     * 下载文件
     *
     * @param fileId 下载文件id
     */
    @Override
    public File download(String fileId) throws IOException, MyException {

        if (fileId == null) {
            return null;
        }

        String[] split = fileId.split("\\.");
        String suffix = "";
        if (split.length >= 2) {
            suffix = "." + split[1];
        }
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID().toString() + suffix);
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(this.downloadGetByteArray(fileId));
        stream.close();
        return file;
    }

    /**
     * 下载文件-获取输入流
     *
     * @param fileId 下载文件id
     */
    @Override
    public InputStream downloadGetStream(String fileId) throws IOException, MyException {
        byte[] bytes = this.downloadGetByteArray(fileId);
        if (bytes == null) {
            return null;
        }
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 下载文件-获取字节数组
     */
    private byte[] downloadGetByteArray(String fileId) throws IOException, MyException {
        TrackerServer trackerServer = this.getConnection();
        StorageServer storeStorage = this.tracker.getStoreStorage(trackerServer, null);
        StorageClient1 client = new StorageClient1(trackerServer, storeStorage);
        byte[] result = client.download_file1(fileId);
        trackerServer.close();
        return result;
    }

    /**
     * 获取文件详细信息
     *
     * @param fileId 文件id
     */
    @Override
    public FileInfo getFileInfo(String fileId) throws IOException, MyException {

        if (fileId == null) {
            return null;
        }

        String group_name = fileId.substring(0, fileId.indexOf("/"));

        TrackerServer trackerServer = this.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        FileInfo file_info = storageClient.get_file_info(group_name, fileId);
        trackerServer.close();
        return file_info;
    }

    /**
     * 获取文件详细信息
     *
     * @param fileId 文件id
     */
    @Override
    public NameValuePair[] getMeta(String fileId) throws IOException, MyException {

        if (fileId == null) {
            return null;
        }

        String group_name = fileId.substring(0, fileId.indexOf("/"));
        String remote_filename = fileId.substring(fileId.indexOf("/") + 1);
        TrackerServer trackerServer = this.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);


        NameValuePair[] metadata = storageClient.get_metadata(group_name, remote_filename);
        trackerServer.close();
        return metadata;
    }

    /**
     * 删除文件
     *
     * @param fileId 文件id
     */
    @Override
    public boolean deleted(String fileId) throws IOException, MyException {
        TrackerServer trackerServer = this.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer,
                null);
        String group_name = fileId.substring(0, fileId.indexOf("/"));
        String remote_filename = fileId.substring(fileId.indexOf("/") + 1);
        int i = storageClient.delete_file(group_name, remote_filename);
        trackerServer.close();
        return i == 0;
    }

    /**
     * 设置元数据
     *
     * @param file 文件对象
     */
    @Override
    public boolean setMeta(DFSFile file) throws IOException, MyException {

        String fileId = file.getFileId();
        if (fileId == null) {
            return false;
        }

        Set<Map.Entry<String, String>> meta = file.getMeta();

        NameValuePair[] metaList = null;
        if (meta != null) {
            metaList = new NameValuePair[meta.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : meta) {
                metaList[i++] = new NameValuePair(entry.getKey(), entry.getValue());
            }
        }

        if (metaList == null) {
            return false;
        }

        TrackerServer trackerServer = this.getConnection();
        StorageClient1 client = new StorageClient1(trackerServer, null);
        int f = client.set_metadata1(fileId, metaList, ProtoCommon.STORAGE_SET_METADATA_FLAG_MERGE);
        trackerServer.close();
        file.setSetMeta(f == 0);
        return f == 0;
    }
}
