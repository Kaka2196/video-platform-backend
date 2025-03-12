package com.yl.videoplatformbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yl.videoplatformbackend.common.GlobalCodeEnum;
import com.yl.videoplatformbackend.common.GlobalException;
import com.yl.videoplatformbackend.common.R;
import com.yl.videoplatformbackend.entity.DTO.SessionAddRequest;
import com.yl.videoplatformbackend.entity.DTO.SessionUpdateRequest;
import com.yl.videoplatformbackend.entity.Session;
import com.yl.videoplatformbackend.entity.User;
import com.yl.videoplatformbackend.entity.VO.SessionVO;
import com.yl.videoplatformbackend.service.SessionService;
import com.yl.videoplatformbackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/session")
@Slf4j
public class SessionController {

    @Autowired
    private UserService userService;
    @Autowired
    private SessionService sessionService;

    @PostMapping("/add")
    @Transactional
    public R<Boolean> add(@RequestBody SessionAddRequest sessionAddRequest, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        if(sessionAddRequest == null){
            throw new GlobalException(GlobalCodeEnum.EX_PARAMS);
        }
        int sessionId = sessionAddRequest.getSessionId();
        // sessionId 为好友Id
        User user = userService.getById(sessionId);
        if(user == null){
            throw new GlobalException(GlobalCodeEnum.EX_PARAMS,"用户不存在");
        }
        int userId = loginUser.getId();
        // 查询当前会话是否存在
        List<Session> sessionList = sessionService.getSessionList(userId);
        List<Integer> id = sessionList.stream().map(Session::getSessionId).toList();
        boolean contains = id.contains(sessionId);
        if(contains){
            return R.success(true);
        }
        // 两个会话
        Session session = new Session();
        session.setUserId(userId);
        session.setSessionId(sessionId);
        session.setType(1);
        Session session2 = new Session();
        session2.setUserId(sessionId);
        session2.setSessionId(userId);
        session2.setType(1);

        boolean save = sessionService.save(session);
        boolean save2 = sessionService.save(session2);
        if(!save || !save2){
            throw new GlobalException(GlobalCodeEnum.EX_SYSTEM,"会话添加失败");
        }
        return R.success(true);
    }
    @GetMapping("/list")
    public R<List<SessionVO>> list(HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        int userId = loginUser.getId();
        List<Session> sessionList = sessionService.getSessionList(userId);
        try{
            List<SessionVO> voList = sessionList.stream().map(session -> {
                User user = userService.getById(session.getSessionId());
                SessionVO sessionVO = new SessionVO();
                sessionVO.setId(session.getId());
                sessionVO.setUserId(session.getUserId());
                sessionVO.setSessionId(session.getSessionId());
                sessionVO.setUserName(user.getName());
                sessionVO.setUserAvatar(user.getAvatar());
                sessionVO.setLastTime(session.getCreateTime());
                sessionVO.setLastMessage(session.getLastMessage());
                return sessionVO;
            }).collect(Collectors.toList());
            return R.success(voList);
        }catch (Exception e){
            throw new GlobalException(GlobalCodeEnum.EX_PARAMS, "会话不存在");
        }
    }

    @DeleteMapping("/delete")
    public R<Boolean> delete(long id){
        Session session = sessionService.getById(id);
        if(session == null){
            throw new GlobalException(GlobalCodeEnum.EX_PARAMS,"会话不存在");
        }
        boolean flag = sessionService.removeById(session);
        return R.success(flag);
    }

    @PutMapping("/update")
    public R<Boolean> update(@RequestBody SessionUpdateRequest sessionUpdateRequest){
        if(sessionUpdateRequest == null){
            throw new GlobalException(GlobalCodeEnum.EX_PARAMS);
        }
        long senderId = sessionUpdateRequest.getSenderId();
        long receiverId = sessionUpdateRequest.getReceiverId();

        QueryWrapper<Session> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",senderId).eq("sessionId",receiverId)
                .or().eq("userId",receiverId).eq("sessionId",senderId);
        try {
            List<Session> sessionList = sessionService.list(queryWrapper);
            sessionList.forEach(session -> {
                session.setLastMessage(sessionUpdateRequest.getLastMessage());
                session.setCreateTime(new Date());
                sessionService.updateById(session);
            });
        }catch (Exception e){
            log.error(e.getMessage());
            throw new GlobalException(GlobalCodeEnum.EX_PARAMS,"会话更新失败");
        }

        return R.success(true);
    }
}
