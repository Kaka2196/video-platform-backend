package com.yl.videoplatformbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yl.videoplatformbackend.common.GlobalCodeEnum;
import com.yl.videoplatformbackend.common.GlobalException;
import com.yl.videoplatformbackend.common.R;
import com.yl.videoplatformbackend.entity.DTO.VideoAddRequest;
import com.yl.videoplatformbackend.entity.DTO.VideoSearchRequest;
import com.yl.videoplatformbackend.entity.*;
import com.yl.videoplatformbackend.entity.DTO.VideoThumbAddRequest;
import com.yl.videoplatformbackend.entity.VO.VideoCardVO;
import com.yl.videoplatformbackend.entity.VO.VideoVO;
import com.yl.videoplatformbackend.service.*;
import com.yl.videoplatformbackend.utils.WatermarkGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/videoThumb")
public class VideoThumbController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private VideoThumbService videoThumbService;

    /**
     * 点赞、取消点赞
     */
    @PostMapping("/add")
    public R<Boolean> addThumb(@RequestBody VideoThumbAddRequest videoThumbAddRequest) {
        Integer videoId = videoThumbAddRequest.getVideoId();
        Integer userId = videoThumbAddRequest.getUserId();
        LambdaQueryWrapper<VideoThumb> qw = new LambdaQueryWrapper<>();
        qw.eq(VideoThumb::getVideoId, videoId).eq(VideoThumb::getUserId, userId);
        VideoThumb one = videoThumbService.getOne(qw);
        if(one == null){
            VideoThumb videoThumb = new VideoThumb();
            videoThumb.setVideoId(videoId);
            videoThumb.setUserId(userId);
            videoThumbService.save(videoThumb);
        }else {
            videoThumbService.remove(qw);
        }
        return R.success();
    }

    @PutMapping("/updateAddThumb")
    public R<Boolean> updateAddThumb(Integer videoId) {
        Video video = videoService.getById(videoId);
        if(video == null){
            return R.fail(GlobalCodeEnum.EX_PARAMS, "视频数据不存在");
        }
        video.setLikes(video.getLikes() + 1);
        return R.success(videoService.updateById(video));
    }

    @PutMapping("/updateMinThumb")
    public R<Boolean> updateMinThumb(Integer videoId) {
        Video video = videoService.getById(videoId);
        if(video == null){
            return R.fail(GlobalCodeEnum.EX_PARAMS, "视频数据不存在");
        }
        video.setLikes(video.getLikes() - 1);
        return R.success(videoService.updateById(video));
    }
}
