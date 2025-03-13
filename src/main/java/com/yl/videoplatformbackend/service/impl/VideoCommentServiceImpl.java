package com.yl.videoplatformbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.videoplatformbackend.entity.User;
import com.yl.videoplatformbackend.entity.VO.VideoCommentVO;
import com.yl.videoplatformbackend.entity.VideoComment;
import com.yl.videoplatformbackend.service.UserService;
import com.yl.videoplatformbackend.service.VideoCommentService;
import com.yl.videoplatformbackend.mapper.VideoCommentMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author Leon
* @description 针对表【videoComment】的数据库操作Service实现
* @createDate 2025-03-13 00:37:06
*/
@Service
public class VideoCommentServiceImpl extends ServiceImpl<VideoCommentMapper, VideoComment>
    implements VideoCommentService {

    @Autowired
    private UserService userService;

    @Override
    public List<VideoCommentVO> getVideoVOList(int videoId) {
        LambdaQueryWrapper<VideoComment> qw = new LambdaQueryWrapper<>();
        qw.eq(VideoComment::getVideoId, videoId).eq(VideoComment::getRootCommentId, -1);
        List<VideoComment> videoCommentList = list(qw);
        if(videoCommentList == null || videoCommentList.isEmpty()){
            return new ArrayList<VideoCommentVO>();
        }
        List<VideoCommentVO> videoCommentVOList = new ArrayList<>();
        // 准备批量查询User
        Set<Integer> userIds = new HashSet<>();

        videoCommentList.forEach(videoComment -> {
            VideoCommentVO videoCommentVO = new VideoCommentVO();
            BeanUtils.copyProperties(videoComment, videoCommentVO);
            userIds.add(videoComment.getUserId());

            List<VideoComment> replys = list(new LambdaQueryWrapper<VideoComment>()
                    .eq(VideoComment::getRootCommentId, videoComment.getId()));
            List<VideoCommentVO> replyVOs = new ArrayList<>();
            if(!replys.isEmpty()){
                Set<Integer> replyUserIds = new HashSet<>();

                replys.forEach(reply -> {
                    VideoCommentVO replyVO = new VideoCommentVO();
                    BeanUtils.copyProperties(reply, replyVO);
                    replyUserIds.add(reply.getUserId());

                    replyVO.setHasThumb(false);
                    replyVOs.add(replyVO);
                });
                // 回复评论填充用户信息
                List<User> replyUsers = userService.listByIds(replyUserIds);
                Map<Integer, User> replyUserMap = replyUsers.stream().collect(Collectors.toMap(User::getId, user -> user));
                replyVOs.forEach(replyVO -> replyVO.setUser(replyUserMap.get(replyVO.getUserId())));
            }

            videoCommentVO.setReplyList(replyVOs);
            videoCommentVO.setHasThumb(false);
            videoCommentVOList.add(videoCommentVO);
        });

        // 根评论填充用户信息
        List<User> users = userService.listByIds(userIds);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));
        videoCommentVOList.forEach(videoCommentVO -> videoCommentVO.setUser(userMap.get(videoCommentVO.getUserId())));

        return videoCommentVOList;
    }
}




