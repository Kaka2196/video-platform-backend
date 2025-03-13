package com.yl.videoplatformbackend.entity.VO;

import com.yl.videoplatformbackend.entity.User;
import com.yl.videoplatformbackend.entity.VideoComment;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 帖子视图
 *
 * 
 */
@Data
public class VideoCommentVO implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     *
     */
    private Date createTime;

    private Integer userId;
    /**
     * 创建人信息
     */
    private User user;

    /**
     * 是否已点赞
     */
    private Boolean hasThumb;

    /**
     * 回复评论数据
     */
    private List<VideoCommentVO> replyList;

}
