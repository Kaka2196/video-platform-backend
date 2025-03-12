package com.yl.videoplatformbackend.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName videoType
 */
@Data
public class VideoType implements Serializable {
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
    private Integer typeId;

    private static final long serialVersionUID = 1L;
}