package com.yl.videoplatformbackend.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yl.videoplatformbackend.common.GlobalCodeEnum;
import com.yl.videoplatformbackend.common.GlobalException;
import com.yl.videoplatformbackend.common.R;
import com.yl.videoplatformbackend.entity.DTO.UserRegisterRequest;
import com.yl.videoplatformbackend.entity.User;
import com.yl.videoplatformbackend.service.IUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author yl
 * @since 2024-11-28
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/list")
    public R<List<User>> list() {
        return R.success(userService.list());
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        if (user == null || user.getAccount() == null || user.getPassword() == null) {
            return R.fail(GlobalCodeEnum.EX_PARAMS);
        }
        return R.success(userService.login(user,request,response));
    }
    @GetMapping("/getLoginUser")
    public R<User> getLoginUser(HttpServletRequest request) {
        return R.success(userService.getLoginUser(request));
    }
    @PostMapping("/register")
    public R<Boolean> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        return R.success(userService.register(userRegisterRequest));
    }
    @GetMapping("/logout")
    public R<Boolean> logout(HttpServletRequest request,HttpServletResponse response) {

        return R.success(userService.logout(request,response));
    }
}
