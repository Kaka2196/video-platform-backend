package com.yl.videoplatformbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.videoplatformbackend.entity.Session;
import com.yl.videoplatformbackend.service.SessionService;
import com.yl.videoplatformbackend.mapper.SessionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Leon
* @description 针对表【session】的数据库操作Service实现
* @createDate 2025-03-12 01:44:47
*/
@Service
public class SessionServiceImpl extends ServiceImpl<SessionMapper, Session>
    implements SessionService{
    @Override
    public List<Session> getSessionList(int userId) {
        QueryWrapper<Session> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        return list(queryWrapper);
    }
}




