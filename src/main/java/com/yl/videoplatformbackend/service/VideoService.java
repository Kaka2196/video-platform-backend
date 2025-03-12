package com.yl.videoplatformbackend.service;

import com.yl.videoplatformbackend.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

/**
* @author Leon
* @description 针对表【video】的数据库操作Service
* @createDate 2025-02-21 16:46:03
*/
public interface VideoService extends IService<Video> {

    Boolean add(Video video, HttpServletRequest request, MultipartFile videoFile, MultipartFile imgFile);

    void transcodeVideo(Video video, String resolution);
}
