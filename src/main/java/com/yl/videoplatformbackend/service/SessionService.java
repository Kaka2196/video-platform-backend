package com.yl.videoplatformbackend.service;

import com.yl.videoplatformbackend.entity.Session;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Leon
* @description 针对表【session】的数据库操作Service
* @createDate 2025-03-12 01:44:47
*/
public interface SessionService extends IService<Session> {
    List<Session> getSessionList (int userId);

}
