package com.yl.videoplatformbackend.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName videoComment
 */
@Data
public class VideoComment implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer commentId;

    /**
     * 
     */
    private Integer rootCommentId;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private Integer videoId;

    /**
     * 
     */
    private Integer thumbNum;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private String content;

    private static final long serialVersionUID = 1L;
}