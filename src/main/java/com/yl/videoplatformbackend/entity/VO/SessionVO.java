package com.yl.videoplatformbackend.entity.VO;

import lombok.Data;

import java.util.Date;

@Data
public class SessionVO {

    private long id;

    private long userId;

    private long sessionId;

    private String userName;

    private String userAvatar;

    private Date lastTime;

    private String lastMessage;
}