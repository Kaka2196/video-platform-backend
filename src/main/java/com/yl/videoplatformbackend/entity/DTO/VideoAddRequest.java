package com.yl.videoplatformbackend.entity.DTO;

import lombok.Data;

import java.util.List;

@Data
public class VideoAddRequest {
    /**
     *
     */
    private String title;

    /**
     * 简介
     */
    private String intro;

    private List<String> types;

}
