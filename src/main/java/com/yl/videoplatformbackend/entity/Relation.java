package com.yl.videoplatformbackend.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName relation
 */
@Data
public class Relation implements Serializable {
    private Integer id;
    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private Integer fanId;

    /**
     * 
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}