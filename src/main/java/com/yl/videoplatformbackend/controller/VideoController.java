package com.yl.videoplatformbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yl.videoplatformbackend.common.GlobalCodeEnum;
import com.yl.videoplatformbackend.common.GlobalException;
import com.yl.videoplatformbackend.common.R;
import com.yl.videoplatformbackend.entity.*;
import com.yl.videoplatformbackend.entity.DTO.VideoAddRequest;
import com.yl.videoplatformbackend.entity.DTO.VideoSearchRequest;
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
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private VideoResolutionService videoResolutionService;
    @Autowired
    private VideoTypeService videoTypeService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private UserService userService;
    @Qualifier("objectMapper")

    @PostMapping("/add")
    public R<Boolean> addVideo(@RequestPart("video") VideoAddRequest videoAddRequest,
                               @RequestPart("videoFile") MultipartFile videoFile,
                               @RequestPart("imgFile") MultipartFile imgFile,
                               HttpServletRequest request) {
        return R.success(videoService.add(videoAddRequest, request, videoFile, imgFile));
    }

    @PutMapping("/update")
    public R<Boolean> updateVideo(@RequestBody Video video) {
        return R.success(videoService.updateById(video));
    }
    @PutMapping("/updateShow")
    public R<Boolean> updateShowVideo(@RequestParam Integer id) {
        Video video = videoService.getById(id);
        if (video.getShowVideo() == 0) {
            video.setShowVideo(1);
        }else{
            video.setShowVideo(0);
        }
        return R.success(videoService.updateById(video));
    }

    @DeleteMapping("/delete")
    public R<Boolean> deleteVideo(int id) {
        return R.success(videoService.removeById(id));
    }

    @GetMapping("/list")
    public R<List<Video>> listVideo(VideoSearchRequest videoSearchRequest) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Video::getTitle, videoSearchRequest.getKeyword()).eq(Video::getPass, 0).eq(Video::getShowVideo, 0);
        return R.success(videoService.list(queryWrapper));
    }
    @GetMapping("/listManage")
    public R<List<Video>> listVideoManage(VideoSearchRequest videoSearchRequest) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Video::getTitle, videoSearchRequest.getKeyword()).eq(Video::getPass, 1);
        return R.success(videoService.list(queryWrapper));
    }

    @GetMapping("/pass")
    public R<Boolean> pass(int id, int pass) {
        Video byId = videoService.getById(id);
        if (byId == null) {
            return R.success(false);
        }
        // 0未审核 1通过 2不通过
        byId.setPass(pass);
        boolean updateSuccess = videoService.updateById(byId);
        // 通过审核后，异步执行转码任务
        if (updateSuccess && pass == 1) {
            // 判断有无水印
            User user = userService.getById(byId.getUserId());
            if (user.getWaterMark() == null || user.getWaterMark().isEmpty()) {
                // 生成水印
                String waterMarkPath = "C:/Users/Leon/Desktop/server/serverWatermark/" + user.getAccount() + ".png";
                try {
                    WatermarkGenerator.createTextWatermark(user.getAccount() + " || MyTube", waterMarkPath);
                    user.setWaterMark(waterMarkPath);
                    userService.updateById(user);
                } catch (IOException e) {
                    throw new GlobalException(GlobalCodeEnum.EX_SYSTEM, "水印生成失败");
                }
            }
            //  转码任务
            videoService.transcodeVideo(byId, "origin");
            videoService.transcodeVideo(byId, "720p");
            videoService.transcodeVideo(byId, "360p");
        }
        return R.success(updateSuccess);
    }

    @GetMapping("/index")
    public R<List<VideoCardVO>> indexList(VideoSearchRequest videoSearchRequest) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        String keyword = videoSearchRequest.getKeyword();
        queryWrapper.like(Video::getTitle, keyword).eq(Video::getPass, 1).eq(Video::getShowVideo, 0);
        List<Video> videoList = videoService.list(queryWrapper);

        if (videoList == null || videoList.isEmpty()) {
            return R.success();
        }

        String type = videoSearchRequest.getType();
        if (type != null && !type.isEmpty()) {
            Type one = typeService.getOne(new LambdaQueryWrapper<Type>().eq(Type::getName, type));
            if (one != null) {
                Integer typeId = one.getId();
                List<VideoType> videoTypeList = videoTypeService.list(new LambdaQueryWrapper<VideoType>().eq(VideoType::getTypeId, typeId));
                Set<Integer> videoIds = videoTypeList.stream().map(VideoType::getVideoId).collect(Collectors.toSet());
                videoList = videoList.stream().filter(video -> videoIds.contains(video.getId())).toList();
            }
        }

        List<VideoCardVO> videoCardVOS = videoList.stream().map(video -> {
            VideoCardVO vo = new VideoCardVO();
            BeanUtils.copyProperties(video, vo);
            return vo;
        }).collect(Collectors.toList());

        // 批量查询 User，避免 N+1 查询问题
        Set<Integer> userIds = videoCardVOS.stream().map(VideoCardVO::getUserId).collect(Collectors.toSet());
        Map<Integer, User> userMap = userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // 赋值用户信息
        videoCardVOS.forEach(videoCardVO -> videoCardVO.setUser(userMap.get(videoCardVO.getUserId())));

        return R.success(videoCardVOS);
    }

    @GetMapping("/detail/{id}")
    public R<VideoVO> detail(@PathVariable Integer id) {
        Video video = videoService.getById(id);
        if (video == null) {
            return R.fail("视频数据不存在");
        }
        VideoVO videoVO = new VideoVO();
        BeanUtils.copyProperties(video, videoVO);

        videoVO.setUser(Optional.ofNullable(userService.getById(video.getUserId())).orElse(new User()));
        videoVO.setResolution(videoResolutionService.list(
                new LambdaQueryWrapper<VideoResolution>().eq(VideoResolution::getVideoId, id)));

        List<VideoType> videoTypeList = videoTypeService.list(new LambdaQueryWrapper<VideoType>().eq(VideoType::getVideoId, id));
        List<Integer> typeIds = videoTypeList.isEmpty() ? Collections.emptyList()
                : videoTypeList.stream().map(VideoType::getTypeId).toList();
        List<Type> types = typeService.listByIds(typeIds);
        videoVO.setType(types.stream().map(Type::getName).toList());

        return R.success(videoVO);
    }

    @GetMapping("/listUser")
    public R<List<Video>> listUserId(int userId) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Video::getUserId, userId).eq(Video::getPass, 1);
        return R.success(videoService.list(queryWrapper));
    }
}
