package com.yl.videoplatformbackend.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName videoCommentThumb
 */
@Data
public class VideoCommentThumb implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer videoCommentId;

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