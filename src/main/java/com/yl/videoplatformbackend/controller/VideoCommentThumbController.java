package com.yl.videoplatformbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yl.videoplatformbackend.common.GlobalCodeEnum;
import com.yl.videoplatformbackend.common.R;
import com.yl.videoplatformbackend.entity.DTO.VideoCommentThumbAddRequest;
import com.yl.videoplatformbackend.entity.DTO.VideoThumbAddRequest;
import com.yl.videoplatformbackend.entity.Video;
import com.yl.videoplatformbackend.entity.VideoComment;
import com.yl.videoplatformbackend.entity.VideoCommentThumb;
import com.yl.videoplatformbackend.entity.VideoThumb;
import com.yl.videoplatformbackend.service.VideoCommentService;
import com.yl.videoplatformbackend.service.VideoCommentThumbService;
import com.yl.videoplatformbackend.service.VideoService;
import com.yl.videoplatformbackend.service.VideoThumbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/videoCommentThumb")
public class VideoCommentThumbController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private VideoCommentService videoCommentService;
    @Autowired
    private VideoCommentThumbService videoCommentThumbService;

    /**
     * 点赞、取消点赞
     */
    @PostMapping("/add")
    public R<Boolean> addThumb(@RequestBody VideoCommentThumbAddRequest videoCommentThumbAddRequest) {
        Integer videoCommentId = videoCommentThumbAddRequest.getVideoCommentId();
        Integer userId = videoCommentThumbAddRequest.getUserId();
        LambdaQueryWrapper<VideoCommentThumb> qw = new LambdaQueryWrapper<>();
        qw.eq(VideoCommentThumb::getVideoCommentId, videoCommentId).eq(VideoCommentThumb::getUserId, userId);
        VideoCommentThumb one = videoCommentThumbService.getOne(qw);
        if(one == null){
            VideoCommentThumb videoCommentThumb = new VideoCommentThumb();
            videoCommentThumb.setVideoCommentId(videoCommentId);
            videoCommentThumb.setUserId(userId);
            videoCommentThumbService.save(videoCommentThumb);
        }else {
            videoCommentThumbService.remove(qw);
        }
        return R.success();
    }

    @PutMapping("/updateAddThumb")
    public R<Boolean> updateAddThumb(Integer videoCommentId) {
        VideoComment videoComment = videoCommentService.getById(videoCommentId);
        if(videoComment == null){
            return R.fail(GlobalCodeEnum.EX_PARAMS, "视频评论数据不存在");
        }
        videoComment.setThumbNum(videoComment.getThumbNum() + 1);
        return R.success(videoCommentService.updateById(videoComment));
    }

    @PutMapping("/updateMinThumb")
    public R<Boolean> updateMinThumb(Integer videoCommentId) {
        VideoComment videoComment = videoCommentService.getById(videoCommentId);
        if(videoComment == null){
            return R.fail(GlobalCodeEnum.EX_PARAMS, "视频评论数据不存在");
        }
        videoComment.setThumbNum(videoComment.getThumbNum() - 1);
        return R.success(videoCommentService.updateById(videoComment));
    }
}
