package com.yl.videoplatformbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yl.videoplatformbackend.common.GlobalCodeEnum;
import com.yl.videoplatformbackend.common.R;
import com.yl.videoplatformbackend.entity.Relation;
import com.yl.videoplatformbackend.entity.User;
import com.yl.videoplatformbackend.entity.UserHistory;
import com.yl.videoplatformbackend.entity.Video;
import com.yl.videoplatformbackend.service.RelationService;
import com.yl.videoplatformbackend.service.UserHistoryService;
import com.yl.videoplatformbackend.service.UserService;
import com.yl.videoplatformbackend.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
@RequestMapping("/relation")
public class RelationController {

    @Autowired
    private RelationService relationService;
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public R<Boolean> addOrRemove(@RequestBody Relation relation) {
        if (relation == null || relation.getUserId() == -1) {
            return R.fail(GlobalCodeEnum.EX_PARAMS, "无数据");
        }
        Integer fanId = relation.getFanId();
        Integer userId = relation.getUserId();
        LambdaQueryWrapper<Relation> qw = new LambdaQueryWrapper<>();
        qw.eq(Relation::getFanId, userId).eq(Relation::getUserId, fanId);
        Relation one = relationService.getOne(qw);
        if (one == null) {
            Relation result = new Relation();
            result.setFanId(userId);
            result.setUserId(fanId);
            return R.success(relationService.save(result));
        } else {
            return R.success(relationService.removeById(one));
        }
    }

    @GetMapping("/isFollowed")
    public R<Boolean> isFollowed(Integer userId, Integer fanId) {
        if (userId == null || userId == -1) {
            return R.fail(GlobalCodeEnum.EX_PARAMS, "无数据");
        }
        Relation one = relationService.getOne(new LambdaQueryWrapper<Relation>()
                .eq(Relation::getFanId, userId).eq(Relation::getUserId, fanId));
        boolean flag = one != null;
        return R.success(flag);
    }
    @GetMapping("/fanList")
    public R<List<User>> fanList(int userId) {
        if (userId == -1) {
            return R.fail(GlobalCodeEnum.EX_PARAMS, "无数据");
        }
        List<Relation> relations = relationService.list(new LambdaQueryWrapper<Relation>().eq(Relation::getUserId, userId));
        if (relations.isEmpty()) {
            return R.success(null);
        }
        List<Integer> fanIds = relations.stream().map(Relation::getFanId).toList();
        return R.success(userService.listByIds(fanIds));
    }

    @GetMapping("/subscribeList")
    public R<List<User>> subscribeList(int userId) {
        if (userId == -1) {
            return R.fail(GlobalCodeEnum.EX_PARAMS, "无数据");
        }
        List<Relation> relations = relationService.list(new LambdaQueryWrapper<Relation>().eq(Relation::getFanId, userId));
        if (relations.isEmpty()) {
            return R.success(null);
        }
        List<Integer> subIds = relations.stream().map(Relation::getUserId).toList();
        return R.success(userService.listByIds(subIds));
    }

    @GetMapping("/getCount")
    public R<List<Integer>> getCount(int userId) {
        int followers = (int)relationService.count(new LambdaQueryWrapper<Relation>().eq(Relation::getUserId, userId));
        int following = (int)relationService.count(new LambdaQueryWrapper<Relation>().eq(Relation::getFanId, userId));
        List<Integer> list = new ArrayList<>();
        list.add(followers);
        list.add(following);
        return R.success(list);
    }
}
