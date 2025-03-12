package com.yl.videoplatformbackend.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName session
 */
@Data
public class Session implements Serializable {
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
    private Integer sessionId;

    /**
     * 
     */
    private Integer type;

    /**
     * 
     */
    private String lastMessage;

    /**
     * 
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}