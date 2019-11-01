package com.ch.fastdfsapi.controller;

import com.ch.fastdfsapi.model.entity.FileBean;
import com.ch.fastdfsapi.services.FileServers;
import com.ch.fastdfsapi.utils.Msg;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据类 接口
 *
 * @author ch
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FilBeanController {

    @Autowired
    private FileServers fileServers;

    @ApiOperation("删除")
    @GetMapping("/deleted")
    public Msg deleted(@RequestParam String id) {
        return fileServers.deleted(id);
    }

    @ApiOperation("分页查询")
    @GetMapping("/find/page")
    public Page<FileBean> findByPage(@RequestParam int page, @RequestParam int size) {
        return fileServers.findByPage(page, size);
    }
}
