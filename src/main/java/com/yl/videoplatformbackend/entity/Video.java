package com.yl.videoplatformbackend.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName video
 */
@Data
@TableName("video")
public class Video implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
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