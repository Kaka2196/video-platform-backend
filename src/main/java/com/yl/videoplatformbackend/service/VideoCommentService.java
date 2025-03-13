package com.yl.videoplatformbackend.service;

import com.yl.videoplatformbackend.entity.VO.VideoCommentVO;
import com.yl.videoplatformbackend.entity.VideoComment;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author Leon
* @description 针对表【videoComment】的数据库操作Service
* @createDate 2025-03-13 00:37:06
*/
public interface VideoCommentService extends IService<VideoComment> {


    List<VideoCommentVO> getVideoVOList(int videoId);
}
