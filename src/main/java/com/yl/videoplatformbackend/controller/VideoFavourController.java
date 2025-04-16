package com.yl.videoplatformbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yl.videoplatformbackend.common.GlobalCodeEnum;
import com.yl.videoplatformbackend.common.R;
import com.yl.videoplatformbackend.entity.DTO.VideoThumbAddRequest;
import com.yl.videoplatformbackend.entity.Video;
import com.yl.videoplatformbackend.entity.VideoFavour;
import com.yl.videoplatformbackend.entity.VideoThumb;
import com.yl.videoplatformbackend.service.VideoFavourService;
import com.yl.videoplatformbackend.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/videoFavour")
public class VideoFavourController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private VideoFavourService videoFavourService;

    /**
     * 点赞、取消点赞
     */
    @PostMapping("/add")
    public R<Boolean> addThumb(@RequestBody VideoThumbAddRequest videoThumbAddRequest) {
        Integer videoId = videoThumbAddRequest.getVideoId();
        Integer userId = videoThumbAddRequest.getUserId();
        LambdaQueryWrapper<VideoFavour> qw = new LambdaQueryWrapper<>();
        qw.eq(VideoFavour::getVideoId, videoId).eq(VideoFavour::getUserId, userId);
        VideoFavour one = videoFavourService.getOne(qw);
        if(one == null){
            VideoFavour videoFavour = new VideoFavour();
            videoFavour.setVideoId(videoId);
            videoFavour.setUserId(userId);
            videoFavourService.save(videoFavour);
        }else {
            videoFavourService.remove(qw);
        }
        return R.success();
    }

    @PutMapping("/updateAddFavour")
    public R<Boolean> updateAddFavour(Integer videoId) {
        Video video = videoService.getById(videoId);
        if(video == null){
            return R.fail(GlobalCodeEnum.EX_PARAMS, "视频数据不存在");
        }
        video.setFavor(video.getFavor() + 1);
        return R.success(videoService.updateById(video));
    }

    @PutMapping("/updateMinFavour")
    public R<Boolean> updateMinFavour(Integer videoId) {
        Video video = videoService.getById(videoId);
        if(video == null){
            return R.fail(GlobalCodeEnum.EX_PARAMS, "视频数据不存在");
        }
        video.setFavor(video.getFavor() - 1);
        return R.success(videoService.updateById(video));
    }
}
