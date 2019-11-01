package com.ikingtech.fastdfs.test;

import com.ch.fastdfsapi.services.Client;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

public class Test {

    /**
     * 连接测试类
     */
    public static void main(String[] args) throws Exception {

        Client global = new Client();
        //  找寻配置文件
        URL resource = Test.class.getResource("/application-dev.yml");
        Properties properties = new Properties();
        properties.load(new FileInputStream(new File(resource.getPath())));
        //  初始化配置
        global.init(properties);

        //  建立 tracker 连接
        TrackerClient tracker = new TrackerClient(global.getG_tracker_group());
        TrackerServer trackerServer = tracker.getConnection();

        //  由 tracker 选举出合适的 StorageServer
        StorageServer storeStorage = tracker.getStoreStorage(trackerServer, null);
        StorageClient1 client = new StorageClient1(trackerServer, storeStorage);

        NameValuePair[] metaList = new NameValuePair[1];

        metaList[0] = new NameValuePair("fileName", "");
        //  上传文件
        String fileIds = client.upload_file1("pom.xml",
                "png", metaList);
        //  关闭流
        trackerServer.close();
        System.out.println(fileIds);

    }


}
