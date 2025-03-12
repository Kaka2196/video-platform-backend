package com.yl.videoplatformbackend.controller;

import com.yl.videoplatformbackend.common.GlobalCodeEnum;
import com.yl.videoplatformbackend.common.GlobalException;
import com.yl.videoplatformbackend.common.R;
import com.yl.videoplatformbackend.entity.Message;
import com.yl.videoplatformbackend.entity.User;
import com.yl.videoplatformbackend.entity.VO.MessageListVO;
import com.yl.videoplatformbackend.service.MessageService;
import com.yl.videoplatformbackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @GetMapping("/list/{receiverId}")
    public R<List<MessageListVO>> list(@PathVariable int receiverId, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        int senderId = loginUser.getId();
        try {
            List<Message> messageList = messageService.getMessageList(senderId, receiverId);
            List<MessageListVO> messageListVOList = messageList.stream().map(message -> messageService.toVo(message))
                    .collect(Collectors.toList());
            return R.success(messageListVOList);
        } catch (Exception e) {
            log.error("获取信息失败:{}", e.getMessage());
            throw new GlobalException(GlobalCodeEnum.EX_SYSTEM, "获取信息失败");
        }
    }
}
