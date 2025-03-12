package com.yl.videoplatformbackend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yl.videoplatformbackend.entity.DTO.MessageAddRequest;
import com.yl.videoplatformbackend.entity.Message;
import com.yl.videoplatformbackend.entity.VO.MessageListVO;
import com.yl.videoplatformbackend.service.MessageService;
import com.yl.videoplatformbackend.utils.SpringContextUtils;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/websocket/{userId}")
@Component
@Slf4j
public class WebSocketServer {

    /** 当前在线客户端数量（线程安全的） */
    private static AtomicInteger onlineClientNumber = new AtomicInteger(0);

    /** 当前在线客户端集合（线程安全的）：以键值对方式存储，key是连接的编号userID，value是连接的对象 */
    private static Map<Integer , Session> onlineClientMap = new ConcurrentHashMap<>();

    /**
     * 客户端与服务端连接成功
     * @param session
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") int userId){
        /*
            do something for onOpen
            与当前客户端连接成功时
         */
        onlineClientNumber.incrementAndGet();//在线数+1
        onlineClientMap.put(userId,session);//添加当前连接的session
        log.info("时间[{}]：与用户id:[{}]的用户连接成功，当前连接编号[{}]，当前连接总数[{}]",
                new Date().toLocaleString(),
                userId,
                userId,
                onlineClientNumber);
    }

    /**
     * 客户端与服务端连接关闭
     * @param session
     */
    @OnClose
    public void onClose(Session session,@PathParam("userId") int userId){
        /*
            do something for onClose
            与当前客户端连接关闭时
         */
        onlineClientNumber.decrementAndGet();//在线数-1
        onlineClientMap.remove(userId);//移除当前连接的session
        log.info("时间[{}]：与用户id:[{}]的用户连接关闭，当前连接编号[{}]，当前连接总数[{}]",
                new Date().toLocaleString(),
                userId,
                userId,
                onlineClientNumber);
    }

    /**
     * 客户端与服务端连接异常
     * @param error
     * @param session
     */
    @OnError
    public void onError(Throwable error,Session session,@PathParam("userId") int userId) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 客户端向服务端发送消息
     * @param message
     * @throws IOException
     */
    @OnMessage
    public void onMsg(Session session,String message,@PathParam("userId") int userId) throws IOException {
        /*
            do something for onMessage
            收到来自当前客户端的消息时
         */
        log.info("时间[{}]：来自连接编号为[{}]的消息：[{}]",
                new Date().toLocaleString(),
                userId,
                message);
        sendAllMessage(message);
    }

    private void sendAllMessage(String message) {
        try {
            ObjectMapper objectMapper = SpringContextUtils.getBean(ObjectMapper.class);
            MessageAddRequest messageAddRequest = objectMapper.readValue(message, MessageAddRequest.class);
            int senderId = messageAddRequest.getSenderId();
            int receiverId = messageAddRequest.getReceiverId();
            String content = messageAddRequest.getContent();
            Integer type = messageAddRequest.getType();

            // 0私聊 1群聊
            Integer isGroup = messageAddRequest.getIsGroup();

            try{
                Message msg = new Message();
                BeanUtils.copyProperties(messageAddRequest,msg);
                MessageService messageService = SpringContextUtils.getBean(MessageService.class);
                messageService.save(msg);
                if(isGroup == 0){
                    Session senderSession = onlineClientMap.get(senderId);
                    Session receiverSession = onlineClientMap.get(receiverId);
                    // 传输数据为 MessageListVO
                    MessageListVO vo = messageService.toVo(msg);
                    senderSession.getAsyncRemote().sendText(objectMapper.writeValueAsString(vo));
                    if(receiverSession != null){
                        receiverSession.getAsyncRemote().sendText(objectMapper.writeValueAsString(vo));
                    }
                    log.info("对方未上线");
                }
            }catch (Exception e){
                log.error("消息发送失败",e);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}