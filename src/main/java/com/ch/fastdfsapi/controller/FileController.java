package com.ch.fastdfsapi.controller;

import cn.hutool.core.util.IdUtil;
import com.ch.fastdfsapi.model.DeleteParameter;
import com.ch.fastdfsapi.model.entity.FileBean;
import com.ch.fastdfsapi.model.parameter.FileBeanParameter;
import com.ch.fastdfsapi.services.FastDFSApi;
import com.ch.fastdfsapi.services.FileServers;
import com.ch.fastdfsapi.utils.FileUtil;
import com.ch.fastdfsapi.utils.Msg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.csource.common.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

@RequestMapping("/api")
@RestController
@Api("OSS 对象存储")
@CrossOrigin
public class FileController {

    @Autowired
    private FastDFSApi fastDFSApi;

    @Autowired
    private FileServers fileServers;

    @Value("${file_path}")
    private String path;

    private Logger log = LoggerFactory.getLogger(FileController.class);

    @ApiOperation("基础文件上传")
    @PostMapping("/upload/v1")
    public Msg uploadV1(@RequestBody MultipartFile file) {

        if (file == null) {
            return Msg.fail("未接受到相应文件");
        }
        Msg<String> upload;
        try {
            String fileName = file.getOriginalFilename();
            if (StringUtils.isEmpty(fileName)) {
                return Msg.fail("未成功获取文件名称");
            }
            log.info("上传文件名称 : " + fileName);
            byte[] bytes = file.getBytes();
            upload = fastDFSApi.upload(bytes, fileName);

        } catch (IOException | MyException e) {
            e.printStackTrace();
            return Msg.fail(e.getMessage());
        }

        if (upload != null && upload.isSuccess()) {
            String fileName = file.getOriginalFilename();
            String[] split = fileName.split("\\.");
            String suffix;
            if (split.length == 2) {
                suffix = split[1];
            } else {
                suffix = split[0];
            }
            String data = upload.getData();
            FileBeanParameter fileBeanParameter = new FileBeanParameter();
            fileBeanParameter.setFileId(data);
            fileBeanParameter.setFileName(fileName);
            fileBeanParameter.setCreateTime(new Date());
            fileBeanParameter.setFileSize(file.getSize());
            fileBeanParameter.setVisitsCount(0L);
            fileBeanParameter.setId(IdUtil.simpleUUID());
            fileBeanParameter.setSuffix(suffix);
            try {
                fileServers.insert(fileBeanParameter);
            } catch (Exception e) {
                log.error("文件元数据存储失败 {} ", e.toString());
            }
        }
        return upload;
    }

    /**
     * 基础文件下载
     */
    @ApiOperation("基础文件下载")
    @GetMapping("/download/v1")
    public Msg downloadV1(@RequestParam String fileId, HttpServletResponse response) {
        if (StringUtils.isEmpty(fileId)) {
            return Msg.fail("未提供相应文件标识");
        }

        InputStream inputStream;
        try {
            log.info("文件下载  文件id: {}", fileId);
            inputStream = fastDFSApi.downloadGetStream(fileId);
            if (inputStream == null) {
                return Msg.fail("未找到相应文件");
            }
            FileUtil.downloadFile(inputStream, fileId, response);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }

        // 文件访问次数 +1
        Optional<FileBean> optional = fileServers.getByFileId(fileId);
        if (optional.isPresent()) {
            FileBean fileBean = optional.get();
            fileBean.setVisitsCount(fileBean.getVisitsCount() + 1);
            try {
                fileServers.save(fileBean);
            } catch (Exception e) {
                log.error("文件元数据访问次数增加失败 {}", e.toString());
            }
        }

        return Msg.sucess();
    }

    /**
     * 基础文件删除
     */
    @ApiOperation("基础文件删除")
    @PostMapping("/deleted/v1")
    public Msg deleted(@RequestBody DeleteParameter parameter) {
        if (parameter == null || StringUtils.isEmpty(parameter.getFileId())) {
            return Msg.fail("未提供相应文件标识");
        }
        String fileId = parameter.getFileId();

        boolean deleted = false;
        try {
            deleted = fastDFSApi.deleted(fileId);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }

        try {
            fileServers.deletedByFileId(fileId);
        } catch (Exception e) {
            log.error("文件元数据删除失败 {}", e.toString());
        }

        log.info("文件删除情况 {}", deleted);
        return Msg.sucess();
    }
}
