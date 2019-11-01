package com.ch.fastdfsapi.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 文件元数据
 *
 * @author ch
 */
@Entity
@Table(name = "file_bean")
@Data
public class FileBean {

    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 文件名称
     */
    @Column(name = "FILE_NAME")
    private String fileName;

    /**
     * 文件大小
     */
    @Column(name = "FILE_SIZE")
    private Long fileSize;

    /**
     * 上传日期
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 访问次数
     */
    @Column(name = "VISITS_COUNT")
    private Long visitsCount;

    /**
     * 后缀
     */
    @Column(name = "SUFFIX")
    private String suffix;

    /**
     * fileId
     */
    @Column(name = "FILE_ID")
    private String fileId;

}
