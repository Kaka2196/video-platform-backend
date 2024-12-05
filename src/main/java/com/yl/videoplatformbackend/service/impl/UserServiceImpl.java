package com.yl.videoplatformbackend.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yl.videoplatformbackend.common.GlobalCodeEnum;
import com.yl.videoplatformbackend.common.GlobalException;
import com.yl.videoplatformbackend.common.R;
import com.yl.videoplatformbackend.config.JacksonConfig;
import com.yl.videoplatformbackend.entity.DTO.UserRegisterRequest;
import com.yl.videoplatformbackend.entity.User;
import com.yl.videoplatformbackend.mapper.UserMapper;
import com.yl.videoplatformbackend.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.videoplatformbackend.utils.EncryptPassword;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author yl
 * @since 2024-11-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Value("${login-status.SECRET_KEY}")
    private String SECRET_KEY;
    @Value("${login-status.SALT}")
    private String SALT;
    @Value("${login-status.COOKIE_KEY}")
    private String COOKIE_KEY = "login_token";
    @Value("${login-status.TOKEN_KEY}")
    private String TOKEN_KEY = "userJson";
    @Value("${login-status.SERVICE_NAME}")
    private String SERVICE_NAME = "video-platform-server";
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String login(User user, HttpServletRequest request, HttpServletResponse response) {
        // 校验加密后的密码
        User loginUser = getOne(new LambdaQueryWrapper<User>().eq(User::getAccount, user.getAccount())
                .eq(User::getPassword, EncryptPassword.encryptPassword(user.getPassword(), SALT)));

        if(loginUser == null) {
            throw new GlobalException(GlobalCodeEnum.EX_PARAMS, "登录失败，账号密码错误！");
        }
        String userJson = null;
        try{
            userJson = objectMapper.writeValueAsString(loginUser);
        } catch (JsonProcessingException e) {
            throw new GlobalException(GlobalCodeEnum.EX_PARAMS, "User转换JSON失败: " + e.getMessage());
        }
        // 创建jwt
        long expirationTimeInMillis = System.currentTimeMillis() + 60 * 60 * 1000; // 1小时后过期，可配置化
        Date expirationDate = new Date(expirationTimeInMillis);

        String token = null;
        try{
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            token = JWT.create()
                    .withIssuedAt(new Date())
                    .withIssuer(SERVICE_NAME)
                    .withSubject(loginUser.getAccount() + " Info")
                    .withExpiresAt(expirationDate)
                    .withClaim(TOKEN_KEY,userJson)
                    .sign(algorithm);
            Cookie cookie = new Cookie(COOKIE_KEY, token);
            cookie.setPath("/app");
            cookie.setMaxAge(3600);
            response.addCookie(cookie);
        }catch (Exception e){
            throw new GlobalException(GlobalCodeEnum.EX_SYSTEM,e.getMessage());
        }
        return token;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        // 获取cookie中的token
        try{
            if(cookies != null) {
                for(Cookie cookie : cookies){
                    if(cookie.getName().equals(COOKIE_KEY)){
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }catch (Exception e){
            throw new GlobalException(GlobalCodeEnum.EX_SYSTEM,e.getMessage());
        }
        if(token == null){
            return null;
        }
        // 解码
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .withIssuer(SERVICE_NAME)
                .build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        // 获取名为userInfo的声明（Claim）
        Claim userInfoClaim = decodedJWT.getClaim(TOKEN_KEY);
        // 判断声明是否为Map类型（因为自定义声明中可以有多种类型数据）
        if(userInfoClaim == null){
            return null;
        }
        try{
            return objectMapper.readValue(userInfoClaim.asString(), User.class);

        }catch (JsonProcessingException e){
            throw new GlobalException(GlobalCodeEnum.EX_PARAMS, "JSON转换User失败: " + e.getMessage());
        }
    }

    @Override
    public Boolean register(UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest == null){
            throw new GlobalException(GlobalCodeEnum.EX_PARAMS);
        }
        String account = userRegisterRequest.getAccount();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if("".equals(account) || "".equals(password) || "".equals(checkPassword)){
            throw new GlobalException(GlobalCodeEnum.EX_PARAMS,"账号密码为空");
        }
        if(!password.equals(checkPassword)){
            throw new GlobalException(GlobalCodeEnum.SUCCESS,"两次输入的密码不一致");
        }

        User one = getOne(new LambdaQueryWrapper<User>().eq(User::getAccount, account));
        if(one != null){
            throw new GlobalException(GlobalCodeEnum.SUCCESS,"账户已存在，请重新输入");
        }
        // 加密密码
        String encryptPassword = EncryptPassword.encryptPassword(password, SALT);
        User user = new User();
        user.setAccount(account);
        user.setPassword(encryptPassword);
        return this.save(user);
    }

    @Override
    public Boolean logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies!= null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_KEY.equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/app");
                    response.addCookie(cookie);
                }
            }
            return true;
        }
        return false;
    }
}
