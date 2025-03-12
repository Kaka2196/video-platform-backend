package com.yl.videoplatformbackend.service;

import com.yl.videoplatformbackend.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yl.videoplatformbackend.entity.VO.MessageListVO;

import java.util.List;

/**
* @author Leon
* @description 针对表【message】的数据库操作Service
* @createDate 2025-03-12 01:45:05
*/
public interface MessageService extends IService<Message> {
    List<Message> getMessageList (int senderId, int receiverId);

    MessageListVO toVo (Message message);
}
