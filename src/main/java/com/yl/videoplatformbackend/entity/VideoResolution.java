package com.yl.videoplatformbackend.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName videoResolution
 */
@Data
public class VideoResolution implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 对应视频id
     */
    private Integer videoId;

    /**
     * 地址
     */
    private String src;

    /**
     * 0-origin 1-720 2-360
     */
    private Integer type;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}