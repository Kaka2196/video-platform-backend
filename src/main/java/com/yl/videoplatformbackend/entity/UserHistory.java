package com.yl.videoplatformbackend.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName userHistory
 */
@Data
public class UserHistory implements Serializable {
    /**
     * 
     */
    private Integer id;

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
    private Date createTime;

    private static final long serialVersionUID = 1L;
}