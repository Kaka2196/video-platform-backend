package com.yl.videoplatformbackend.entity.VO;

import com.yl.videoplatformbackend.entity.User;
import com.yl.videoplatformbackend.entity.VideoResolution;
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
public class VideoVO implements Serializable {
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

    private User user;

    private List<String> type;

    private List<VideoResolution> resolution;

    private Integer view;

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

    private static final long serialVersionUID = 1L;
}