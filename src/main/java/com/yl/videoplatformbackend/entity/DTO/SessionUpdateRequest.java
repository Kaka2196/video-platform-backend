package com.yl.videoplatformbackend.entity.DTO;

import lombok.Data;

@Data
public class SessionUpdateRequest {

    private int senderId;

    private int receiverId;

    private String lastMessage;
}
