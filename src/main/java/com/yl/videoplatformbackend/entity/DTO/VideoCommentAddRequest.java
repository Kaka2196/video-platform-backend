package com.yl.videoplatformbackend.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoCommentAddRequest implements Serializable {

    /**
     * 回复评论id
     */
    private Integer commentId;

    /**
     * 顶级评论id
     */
    private Integer rootCommentId;

    /**
     * 帖子id
     */
    private Integer videoId;

    /**
     * 评论内容
     */
    private String content;

    private static final long serialVersionUID = 1L;

}
