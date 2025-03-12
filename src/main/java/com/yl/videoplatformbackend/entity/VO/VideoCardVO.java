package com.yl.videoplatformbackend.entity.VO;

import com.yl.videoplatformbackend.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @TableName video
 */
@Data
public class VideoCardVO implements Serializable {
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
    private String title;

    /**
     * 封面地址
     */
    private String img;

    private User user;

    private Integer view;

    /**
     * 
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}