package com.ch.fastdfsapi.model.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 文件存储元数据交互类
 *
 * @author ch
 */
@Data
@ApiModel("文件存储元数据交互类")
public class FileBeanParameter {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("文件名称")
    @NotNull(message = "文件名称 不可为空")
    private String fileName;

    @ApiModelProperty("文件大小")
    @NotNull(message = "文件大小 不可为空")
    private Long fileSize;

    @ApiModelProperty("上传日期")
    @NotNull(message = "上传日期 不可为空")
    private Date createTime;

    @ApiModelProperty("访问次数")
    @NotNull(message = "访问次数 不可为空")
    private Long visitsCount;

    @ApiModelProperty("fileId")
    @NotNull(message = "fileId 不可为空")
    private String fileId;

    @ApiModelProperty("后缀")
    private String suffix;
}
