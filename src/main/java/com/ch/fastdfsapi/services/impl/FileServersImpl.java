package com.ch.fastdfsapi.services.impl;

import cn.hutool.core.util.IdUtil;
import com.ch.fastdfsapi.dao.FileRepository;
import com.ch.fastdfsapi.model.entity.FileBean;
import com.ch.fastdfsapi.model.parameter.FileBeanParameter;
import com.ch.fastdfsapi.services.FileServers;
import com.ch.fastdfsapi.utils.BeanUtil;
import com.ch.fastdfsapi.utils.BeanValidator;
import com.ch.fastdfsapi.utils.Msg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * 文件元数据存储
 *
 * @author ch
 */
@Service
@Slf4j
public class FileServersImpl implements FileServers {

    @Autowired
    private FileRepository repository;


    @Override
    public Msg insert(FileBeanParameter parameter) {
        if (parameter == null) {
            throw new NullPointerException("参数不可为NULL");
        }

        BeanValidator.validate(parameter);
        FileBean fileBean = BeanUtil.copyProperties(parameter, FileBean.class);
        fileBean.setId(IdUtil.simpleUUID());
        repository.save(fileBean);
        return Msg.sucess();
    }

    @Override
    public Msg update(FileBeanParameter parameter) {
        if (parameter == null) {
            throw new NullPointerException("参数不可为NULL");
        }

        BeanValidator.validate(parameter);
        FileBean fileBean = BeanUtil.copyProperties(parameter, FileBean.class);
        repository.save(fileBean);
        return Msg.sucess();
    }

    @Override
    public Msg save(FileBean fileBean) {
        repository.save(fileBean);
        return Msg.sucess();
    }

    @Override
    public Msg deleted(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new NullPointerException("参数不可为空");
        }

        repository.delete(id);
        return Msg.sucess();
    }

    @Override
    public Msg<FileBeanParameter> get(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new NullPointerException("参数不可为空");
        }

        return Msg.sucess(BeanUtil.copyProperties(repository.findOne(id), FileBeanParameter.class));
    }

    @Override
    public Page<FileBean> findByPage(int page, int size) {
        return repository.findAll(new PageRequest(--page, size));
    }

    @Override
    public Optional<FileBean> getByFileId(String fileId) {
        if (StringUtils.isEmpty(fileId)) {
            throw new NullPointerException("参数不可为空");
        }
        return repository.findByFileId(fileId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletedByFileId(String fileId) {
        if (StringUtils.isEmpty(fileId)) {
            throw new NullPointerException("参数不可为空");
        }
        repository.deleteByFileId(fileId);
    }
}
