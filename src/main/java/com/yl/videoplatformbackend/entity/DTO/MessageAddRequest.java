package com.yl.videoplatformbackend.entity.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageAddRequest implements Serializable {


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


    private static final long serialVersionUID = 1L;
}