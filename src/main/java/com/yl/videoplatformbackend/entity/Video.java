package com.yl.videoplatformbackend.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName video
 */
@Data
public class Video implements Serializable {
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
     * 简介
     */
    private String intro;

    /**
     * 封面地址
     */
    private String img;

    /**
     * 视频地址
     */
    private String src;

    /**
     * 
     */
    private Integer likes;

    /**
     * 
     */
    private Integer favor;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    private Integer pass;

    private Integer view;

    private static final long serialVersionUID = 1L;
}