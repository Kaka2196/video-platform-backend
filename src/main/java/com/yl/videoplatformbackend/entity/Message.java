package com.yl.videoplatformbackend.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName message
 */
@Data
public class Message implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer senderId;

    /**
     * 
     */
    private Integer receiverId;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private Integer type;

    /**
     * 
     */
    private Integer status;

    /**
     * 
     */
    private Integer isGroup;

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