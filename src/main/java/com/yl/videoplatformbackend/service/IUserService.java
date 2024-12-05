package com.yl.videoplatformbackend.service;

import com.yl.videoplatformbackend.entity.DTO.UserRegisterRequest;
import com.yl.videoplatformbackend.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author yl
 * @since 2024-11-28
 */
public interface IUserService extends IService<User> {


    String login(User user, HttpServletRequest request, HttpServletResponse response);

    User getLoginUser(HttpServletRequest request);

    Boolean register(UserRegisterRequest userRegisterRequest);

    Boolean logout(HttpServletRequest request, HttpServletResponse response);
}
