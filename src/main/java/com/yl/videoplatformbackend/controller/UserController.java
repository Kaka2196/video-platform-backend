package com.yl.videoplatformbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yl.videoplatformbackend.common.GlobalCodeEnum;
import com.yl.videoplatformbackend.common.R;
import com.yl.videoplatformbackend.entity.DTO.UserRegisterRequest;
import com.yl.videoplatformbackend.entity.User;
import com.yl.videoplatformbackend.service.UserService;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public R<List<User>> list() {
        return R.success(userService.list());
    }

    @GetMapping("/listUsersByKeyword")
    public R<List<User>> listUsersByKeyword(String keyword) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getName, keyword);
        return R.success(userService.list(queryWrapper));
    }
    @PostMapping("/login")
    public R<String> login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        if (user == null || user.getAccount() == null || user.getPassword() == null) {
            return R.fail(GlobalCodeEnum.EX_PARAMS);
        }
        return R.success(userService.login(user, request, response));
    }

    @GetMapping("/getLoginUser")
    public R<User> getLoginUser(HttpServletRequest request) {
        return R.success(userService.getLoginUser(request));
    }

    @GetMapping("/getById")
    public R<User> getById(@RequestParam("id") Integer id) {
        return R.success(userService.getById(id));
    }

    @PostMapping("/register")
    public R<Boolean> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        return R.success(userService.register(userRegisterRequest));
    }

    @GetMapping("/logout")
    public R<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {

        return R.success(userService.logout(request, response));
    }

    @GetMapping("/sendCheckCode")
    public R<Boolean> sendCheckCode(String email) {
        if (email == null || email.isEmpty()) {
            return R.fail(GlobalCodeEnum.EX_PARAMS, "邮箱为空");
        }
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getAccount, email));
        if (user != null) {
            return R.fail(GlobalCodeEnum.EX_PARAMS, "该邮箱已注册！");
        }
        return R.success(userService.sendCheckCode(email));
    }

    @PutMapping("/updateRole")
    public R<Boolean> updateRole(Integer id, Integer role, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null || loginUser.getRole() != 1) {
            return R.fail(GlobalCodeEnum.EX_PARAMS, "无权限");
        }
        User user = userService.getById(id);
        if (user == null) {
            return R.fail(GlobalCodeEnum.EX_PARAMS, "用户不存在");
        }
        if (loginUser.getId() == id){
            return R.fail(GlobalCodeEnum.EX_PARAMS,"不可修改自己");
        }
        if (user.getRole() == 1){
            return R.fail(GlobalCodeEnum.EX_PARAMS,"无权限");
        }
        user.setRole(role);
        return R.success(userService.updateById(user));
    }
}
