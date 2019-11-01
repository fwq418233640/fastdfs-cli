package com.ch.fastdfsapi.services;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerGroup;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;

public class Client {

    private static final int DEFAULT_CONNECT_TIMEOUT = 5; //second
    private static final int DEFAULT_NETWORK_TIMEOUT = 30; //second
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final boolean DEFAULT_HTTP_ANTI_STEAL_TOKEN = false;
    private static final String DEFAULT_HTTP_SECRET_KEY = "FastDFS1234567890";
    private static final int DEFAULT_HTTP_TRACKER_HTTP_PORT = 80;

    private int g_connect_timeout = DEFAULT_CONNECT_TIMEOUT * 1000; //millisecond
    private int g_network_timeout = DEFAULT_NETWORK_TIMEOUT * 1000; //millisecond
    private String g_charset = DEFAULT_CHARSET;
    private boolean g_anti_steal_token = DEFAULT_HTTP_ANTI_STEAL_TOKEN; //if anti-steal token
    private String g_secret_key = DEFAULT_HTTP_SECRET_KEY; //generage token secret key
    private int g_tracker_http_port = DEFAULT_HTTP_TRACKER_HTTP_PORT;

    private TrackerGroup g_tracker_group;


    /**
     * load global variables
     */
    public void init(Environment properties) throws MyException {
        String[] szTrackerServers;
        String[] parts;

        g_connect_timeout =
                StringUtils.isEmpty(properties.getProperty("com.ikingtech.fastdfs-client.connect_timeout")) ? 30000 :
                        Integer.parseInt(properties.getProperty("com.ikingtech.fastdfs-client.connect_timeout"));


        if (g_connect_timeout < 0) {
            g_connect_timeout = DEFAULT_CONNECT_TIMEOUT;
        }
        g_connect_timeout *= 1000; //millisecond

        g_network_timeout =
                StringUtils.isEmpty(properties.getProperty("com.ikingtech.fastdfs-client.network_timeout")) ? 30000 :
                        Integer.parseInt(properties.getProperty("com.ikingtech.fastdfs-client.network_timeout"));
        if (g_network_timeout < 0) {
            g_network_timeout = DEFAULT_NETWORK_TIMEOUT;
        }
        g_network_timeout *= 1000; //millisecond

        g_charset = properties.getProperty("com.ikingtech.fastdfs-client.charset");
        if (g_charset == null || g_charset.length() == 0) {
            g_charset = "ISO8859-1";
        }

        String tracker_server = properties.getProperty("com.ikingtech.fastdfs-client.tracker_server");
        if (StringUtils.isEmpty(tracker_server)) {
            throw new MyException("tracker_server is empty");
        }

        String tracker_port = properties.getProperty("com.ikingtech.fastdfs-client.tracker_port");
        if (StringUtils.isEmpty(tracker_port)) {
            throw new MyException("tracker_port is empty");
        }
        String[] split = tracker_server.split(",");
        szTrackerServers = new String[split.length];

        for (int i = 0; i < split.length; i++) {
            szTrackerServers[i] = split[i] + ":" + tracker_port;
        }
        
        InetSocketAddress[] tracker_servers = new InetSocketAddress[szTrackerServers.length];
        for (int i = 0; i < szTrackerServers.length; i++) {
            parts = szTrackerServers[i].split("\\:", 2);
            if (parts.length != 2) {
                throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
            }

            tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        }
        g_tracker_group = new TrackerGroup(tracker_servers);

        g_tracker_http_port = Integer.parseInt(properties.getProperty("com.ikingtech.fastdfs-client.http.tracker_http_port"));
        g_anti_steal_token = Boolean.parseBoolean(properties.getProperty("com.ikingtech.fastdfs-client.http.anti_steal_token"));
        if (g_anti_steal_token) {
            g_secret_key = properties.getProperty("com.ikingtech.fastdfs-client.http.secret_key");
        }

        String property = properties.getProperty("com.ikingtech.fastdfs-client.storage_server");
        if (!StringUtils.isEmpty(property)) {
            this.g_tracker_group.storage_server = property;
        }
    }

    /**
     * load global variables
     */
    public void init(Properties properties) throws MyException {
        String[] szTrackerServers;
        String[] parts;

        g_connect_timeout =
                StringUtils.isEmpty(properties.getProperty("com.ikingtech.fastdfs-client.connect_timeout")) ? 30000 :
                        Integer.parseInt(properties.getProperty("com.ikingtech.fastdfs-client.connect_timeout"));


        if (g_connect_timeout < 0) {
            g_connect_timeout = DEFAULT_CONNECT_TIMEOUT;
        }
        g_connect_timeout *= 1000; //millisecond

        g_network_timeout =
                StringUtils.isEmpty(properties.getProperty("com.ikingtech.fastdfs-client.network_timeout")) ? 30000 :
                        Integer.parseInt(properties.getProperty("com.ikingtech.fastdfs-client.network_timeout"));
        if (g_network_timeout < 0) {
            g_network_timeout = DEFAULT_NETWORK_TIMEOUT;
        }
        g_network_timeout *= 1000; //millisecond

        g_charset = properties.getProperty("com.ikingtech.fastdfs-client.charset");
        if (g_charset == null || g_charset.length() == 0) {
            g_charset = "ISO8859-1";
        }
        String tracker_server = properties.getProperty("com.ikingtech.fastdfs-client.tracker_server");
        if (StringUtils.isEmpty(tracker_server)) {
            throw new MyException("tracker_server is empty");
        }

        String tracker_port = properties.getProperty("com.ikingtech.fastdfs-client.tracker_port");
        if (StringUtils.isEmpty(tracker_port)) {
            throw new MyException("tracker_port is empty");
        }
        String[] split = tracker_server.split(",");
        szTrackerServers = new String[split.length];

        for (int i = 0; i < split.length; i++) {
            szTrackerServers[i] = split[i] + ":" + tracker_port;
        }


        InetSocketAddress[] tracker_servers = new InetSocketAddress[szTrackerServers.length];
        for (int i = 0; i < szTrackerServers.length; i++) {
            parts = szTrackerServers[i].split("\\:", 2);
            if (parts.length != 2) {
                throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
            }

            tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        }
        g_tracker_group = new TrackerGroup(tracker_servers);

        g_tracker_http_port = Integer.parseInt(properties.getProperty("com.ikingtech.fastdfs-client.http.tracker_http_port"));
        g_anti_steal_token = Boolean.parseBoolean(properties.getProperty("com.ikingtech.fastdfs-client.http.anti_steal_token"));
        if (g_anti_steal_token) {
            g_secret_key = properties.getProperty("com.ikingtech.fastdfs-client.http.secret_key");
        }

        String property = properties.getProperty("com.ikingtech.fastdfs-client.storage_server");
        if (!StringUtils.isEmpty(property)) {
            this.g_tracker_group.storage_server = property;
        }

    }

    /**
     * construct Socket object
     *
     * @param ip_addr ip address or hostname
     * @param port    port number
     * @return connected Socket object
     */
    public Socket getSocket(String ip_addr, int port) throws IOException {
        Socket sock = new Socket();
        sock.setSoTimeout(ClientGlobal.g_network_timeout);
        sock.connect(new InetSocketAddress(ip_addr, port), ClientGlobal.g_connect_timeout);
        return sock;
    }

    /**
     * construct Socket object
     *
     * @param addr InetSocketAddress object, including ip address and port
     * @return connected Socket object
     */
    public Socket getSocket(InetSocketAddress addr) throws IOException {
        Socket sock = new Socket();
        sock.setSoTimeout(ClientGlobal.g_network_timeout);
        sock.connect(addr, ClientGlobal.g_connect_timeout);
        return sock;
    }

    public String configInfo() {
        String trackerServers = "";
        if (g_tracker_group != null) {
            InetSocketAddress[] trackerAddresses = g_tracker_group.tracker_servers;
            for (InetSocketAddress inetSocketAddress : trackerAddresses) {
                if (trackerServers.length() > 0) trackerServers += ",";
                trackerServers += inetSocketAddress.toString().substring(1);
            }
        }
        return "{"
                + "\n  g_connect_timeout(ms) = " + g_connect_timeout
                + "\n  g_network_timeout(ms) = " + g_network_timeout
                + "\n  g_charset = " + g_charset
                + "\n  g_anti_steal_token = " + g_anti_steal_token
                + "\n  g_secret_key = " + g_secret_key
                + "\n  g_tracker_http_port = " + g_tracker_http_port
                + "\n  trackerServers = " + trackerServers
                + "\n}";
    }


    public static int getDefaultConnectTimeout() {
        return DEFAULT_CONNECT_TIMEOUT;
    }

    public static int getDefaultNetworkTimeout() {
        return DEFAULT_NETWORK_TIMEOUT;
    }

    public static String getDefaultCharset() {
        return DEFAULT_CHARSET;
    }

    public static boolean isDefaultHttpAntiStealToken() {
        return DEFAULT_HTTP_ANTI_STEAL_TOKEN;
    }

    public static String getDefaultHttpSecretKey() {
        return DEFAULT_HTTP_SECRET_KEY;
    }

    public static int getDefaultHttpTrackerHttpPort() {
        return DEFAULT_HTTP_TRACKER_HTTP_PORT;
    }

    public int getG_connect_timeout() {
        return g_connect_timeout;
    }

    public int getG_network_timeout() {
        return g_network_timeout;
    }

    public String getG_charset() {
        return g_charset;
    }

    public boolean isG_anti_steal_token() {
        return g_anti_steal_token;
    }

    public String getG_secret_key() {
        return g_secret_key;
    }

    public int getG_tracker_http_port() {
        return g_tracker_http_port;
    }

    public TrackerGroup getG_tracker_group() {
        return g_tracker_group;
    }
}
