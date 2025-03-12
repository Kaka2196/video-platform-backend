package com.yl.videoplatformbackend.entity.VO;

import lombok.Data;

import java.util.Date;

@Data
public class MessageListVO {
    /**
     * 主键id
     */
    private long id;

    /**
     *  发送者id
     */
    private long senderId;

    /**
     *  发送者头像
     */
    private String senderAvatar;

    /**
     *  发送者昵称
     */
    private String senderName;

    /**
     *  接收者id
     */
    private long receiverId;

    /**
     *  发送者头像
     */
    private String receiverAvatar;

    /**
     *  发送者昵称
     */
    private String receiverName;

    /**
     *  消息内容
     */
    private String content;

    /***
     * 消息类型
     */
    private Integer type;

    /**
     * 消息状态
     */
    private Integer status;

    /**
     * 是否群聊  0：单聊 1群聊
     */
    private Integer isGroup;


    private Date createTime;
}