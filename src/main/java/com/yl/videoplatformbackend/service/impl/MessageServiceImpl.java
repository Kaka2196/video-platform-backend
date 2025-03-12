package com.yl.videoplatformbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.videoplatformbackend.entity.Message;
import com.yl.videoplatformbackend.entity.User;
import com.yl.videoplatformbackend.entity.VO.MessageListVO;
import com.yl.videoplatformbackend.service.MessageService;
import com.yl.videoplatformbackend.mapper.MessageMapper;
import com.yl.videoplatformbackend.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Leon
* @description 针对表【message】的数据库操作Service实现
* @createDate 2025-03-12 01:45:05
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

    @Resource
    private UserService userService;

    @Override
    public List<Message> getMessageList(int senderId, int receiverId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("senderId",senderId).eq("receiverId",receiverId)
                .or().eq("senderId",receiverId).eq("receiverId",senderId).orderByAsc("createTime");
        return list(queryWrapper);
    }

    @Override
    public MessageListVO toVo(Message message) {
        MessageListVO messageListVO = new MessageListVO();
        BeanUtils.copyProperties(message,messageListVO);
        User senderUser = userService.getById(message.getSenderId());
        User receiverUser = userService.getById(message.getReceiverId());
        messageListVO.setSenderAvatar(senderUser.getAvatar());
        messageListVO.setSenderName(senderUser.getName());
        messageListVO.setReceiverAvatar(receiverUser.getAvatar());
        messageListVO.setReceiverName(receiverUser.getName());
        return messageListVO;
    }
}




