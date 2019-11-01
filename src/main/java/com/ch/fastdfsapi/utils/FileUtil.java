package com.ch.fastdfsapi.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 文件存储
 *
 * @author Ch
 * @since 1.0
 */
public abstract class FileUtil {

    /**
     * 文件下载
     *
     * @param response fwq
     */
    public static void downloadFile(InputStream stream, String fileName, HttpServletResponse response) {
        OutputStream out = null;
        InputStream in = null;
        try {
            // 获取要下载的文件名
            // 设置content-disposition响应头控制浏览器以下载的形式打开文件
            // 中文文件名要使用URLEncoder.encode方法进行编码，否则会出现文件名乱码
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream");
            if (stream == null) {
                return;
            } else {
                in = stream;
            }

            int len;
            byte[] buffer = new byte[8 * 1024]; // 8kb 缓冲区
            out = response.getOutputStream();
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len); // 将缓冲区的数据输出到客户端浏览器
            }
            out.flush();
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
