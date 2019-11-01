package com.ch.fastdfsapi.dao;

import com.ch.fastdfsapi.model.entity.FileBean;
import com.ch.fastdfsapi.model.parameter.FileBeanParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户测试
 *
 * @author ch
 */
@Repository
public interface FileRepository extends JpaRepository<FileBean, String> {

    /**
     * 根据 fileId 查询
     *
     * @param fileId 文件id
     * @return {@link FileBeanParameter} 对象
     */
    Optional<FileBean> findByFileId(String fileId);

    /**
     * 根据 fileId 删除
     *
     * @param fileId 文件id
     */
    void deleteByFileId(String fileId);
}
