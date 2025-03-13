package com.yl.videoplatformbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yl.videoplatformbackend.common.GlobalCodeEnum;
import com.yl.videoplatformbackend.common.GlobalException;
import com.yl.videoplatformbackend.common.R;
import com.yl.videoplatformbackend.entity.DTO.VideoCommentAddRequest;
import com.yl.videoplatformbackend.entity.User;
import com.yl.videoplatformbackend.entity.VO.VideoCommentVO;
import com.yl.videoplatformbackend.entity.VideoComment;
import com.yl.videoplatformbackend.service.UserService;
import com.yl.videoplatformbackend.service.VideoCommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/videoComment")
public class VideoCommentController {

    @Autowired
    private UserService userService;
    @Autowired
    private VideoCommentService videoCommentService;

    @PostMapping("/add")
    public R<Integer> add(@RequestBody VideoCommentAddRequest videoCommentAddRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null || loginUser.getId() < 1) {
            throw new GlobalException(GlobalCodeEnum.EX_PARAMS, "用户未登录");
        }

        if (videoCommentAddRequest == null) {
            throw new GlobalException(GlobalCodeEnum.EX_PARAMS);
        }
        VideoComment videoComment = new VideoComment();
        videoComment.setUserId(loginUser.getId());
        BeanUtils.copyProperties(videoCommentAddRequest, videoComment);
        if(!videoCommentService.save(videoComment)){
            throw new GlobalException(GlobalCodeEnum.EX_SYSTEM, "评论添加失败");
        }
        int newId = videoComment.getId();

        return R.success(newId);
    }

    @GetMapping("/list")
    public R<List<VideoCommentVO>> list(int videoId){
        return R.success(videoCommentService.getVideoVOList(videoId));
    }
}
