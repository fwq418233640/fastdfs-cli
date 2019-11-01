package com.ch.fastdfsapi.services;

import com.ch.fastdfsapi.model.entity.FileBean;
import com.ch.fastdfsapi.model.parameter.FileBeanParameter;
import com.ch.fastdfsapi.utils.Msg;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * 文件元数据存储
 *
 * @author ch
 */
public interface FileServers {

    /**
     * 新增
     *
     * @param parameter 数据
     * @return 成功与否
     */
    Msg insert(FileBeanParameter parameter);

    /**
     * 修改
     *
     * @param parameter 数据
     * @return 成功与否
     */
    Msg update(FileBeanParameter parameter);

    /**
     * 保存
     *
     * @param fileBean 数据
     * @return 成功与否
     */
    Msg save(FileBean fileBean);

    /**
     * 删除
     *
     * @param id 主键
     * @return 成功与否
     */
    Msg deleted(String id);

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return {@link FileBeanParameter} 对象
     */
    Msg<FileBeanParameter> get(String id);


    /**
     * 分页查询
     *
     * @param page 当前页码
     * @param size 每页显示数量
     * @return {@link FileBean} 集合
     */
    Page<FileBean> findByPage(int page, int size);

    /**
     * 根据 fileId 查询
     *
     * @param fileId 文件id
     * @return {@link FileBeanParameter} 对象
     */
    Optional<FileBean> getByFileId(String fileId);

    /**
     * 删除
     *
     * @param fileId 文件id
     */
    void deletedByFileId(String fileId);
}
