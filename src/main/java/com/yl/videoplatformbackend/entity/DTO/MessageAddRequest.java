package com.yl.videoplatformbackend.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageAddRequest {


    /**
     * 
     */
    private int senderId;

    /**
     * 
     */
    private int receiverId;

    /**
     * 聊天文字
     */
    private String content;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 0私聊 1群聊
     */
    private Integer isGroup;


}