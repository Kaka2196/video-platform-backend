package com.yl.videoplatformbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yl.videoplatformbackend.common.GlobalCodeEnum;
import com.yl.videoplatformbackend.common.R;
import com.yl.videoplatformbackend.entity.DTO.UserRegisterRequest;
import com.yl.videoplatformbackend.entity.User;
import com.yl.videoplatformbackend.entity.UserHistory;
import com.yl.videoplatformbackend.entity.Video;
import com.yl.videoplatformbackend.service.UserHistoryService;
import com.yl.videoplatformbackend.service.UserService;
import com.yl.videoplatformbackend.service.VideoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author yl
 * @since 2024-11-28
 */
@RestController
@RequestMapping("/userHistory")
public class UserHistoryController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private UserHistoryService userHistoryService;

    @PostMapping("/add")
    public R<Boolean> addUserHistory(@RequestBody UserHistory userHistory) {
        if(userHistory == null || userHistory.getUserId() == -1){
            return R.fail(GlobalCodeEnum.EX_PARAMS, "无数据");
        }
        Integer videoId = userHistory.getVideoId();
        Integer userId = userHistory.getUserId();
        LambdaQueryWrapper<UserHistory> qw = new LambdaQueryWrapper<>();
        qw.eq(UserHistory::getUserId, userId).eq(UserHistory::getVideoId, videoId);
        UserHistory history = userHistoryService.getOne(qw);
        boolean flag = true;
        if(history == null){
            flag = userHistoryService.save(userHistory);
        }
        return R.success(flag);
    }

    @GetMapping("/list")
    public R<List<Video>> list(Integer userId) {
        List<UserHistory> histories = userHistoryService.list(new LambdaQueryWrapper<UserHistory>()
                .eq(UserHistory::getUserId, userId)
                .orderByDesc(UserHistory::getCreateTime));
        if (histories.isEmpty()){
            return R.success(null);
        }
        List<Integer> videoIds = histories.stream().map(UserHistory::getVideoId).toList();
        return R.success(videoService.listByIds(videoIds));
    }

    @DeleteMapping("/delete")
    public R<Boolean> deleteUserHistory(Integer userId) {
        return R.success(userHistoryService.remove(new LambdaQueryWrapper<UserHistory>().eq(UserHistory::getUserId, userId)));
    }
}
