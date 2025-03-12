package com.yl.videoplatformbackend.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName type
 */
@Data
public class Type implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String info;

    private static final long serialVersionUID = 1L;
}