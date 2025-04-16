package com.yl.videoplatformbackend.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName videoFavour
 */
@Data
public class VideoFavour implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer videoId;

    /**
     * 
     */
    private Integer userId;

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