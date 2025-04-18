package com.yl.videoplatformbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.videoplatformbackend.common.GlobalCodeEnum;
import com.yl.videoplatformbackend.common.GlobalException;
import com.yl.videoplatformbackend.entity.*;
import com.yl.videoplatformbackend.entity.DTO.VideoAddRequest;
import com.yl.videoplatformbackend.service.*;
import com.yl.videoplatformbackend.mapper.VideoMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Leon
 * @description 针对表【video】的数据库操作Service实现
 * @createDate 2025-02-21 16:46:03
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
        implements VideoService {

    @Autowired
    private UserService userService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private VideoTypeService videoTypeService;
    @Autowired
    private VideoResolutionService videoresolutionService;

    private static final String SERVER_PATH = "C:/Users/Leon/Desktop/server/";

    private static final String MAPPING_URL = "http://localhost:8081/app/server/";

    private static final String VIDEO_RESOLUTION_URL = SERVER_PATH + "serverVideoResolution/";
    private static final String VIDEO_RESOLUTION_DATABASE_URL = MAPPING_URL +"serverVideoResolution/";

    @Override
    public Boolean add(VideoAddRequest videoAddRequest, HttpServletRequest request, MultipartFile videoFile, MultipartFile imgFile) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null || loginUser.getId() < 1) {
            throw new GlobalException(GlobalCodeEnum.EX_PARAMS, "用户未登录");
        }

        Video video = new Video();
        BeanUtils.copyProperties(videoAddRequest, video);
        video.setUserId(loginUser.getId());

        String imgPath = SERVER_PATH + "serverImg/" + imgFile.getOriginalFilename();
        String videoPath = SERVER_PATH + "serverVideo/" + videoFile.getOriginalFilename();
        File imgDest = new File(imgPath);
        File videoDest = new File(videoPath);
        try {
            imgFile.transferTo(imgDest);
            videoFile.transferTo(videoDest);
        } catch (IOException e) {
            throw new GlobalException(GlobalCodeEnum.EX_SYSTEM, "存储视频封面失败: " + e.getMessage());
        }
        video.setImg(MAPPING_URL + "serverImg/" + imgFile.getOriginalFilename());
        video.setSrc(MAPPING_URL + "serverVideo/" + videoFile.getOriginalFilename());
        save(video);

        List<String> types = videoAddRequest.getTypes();
        if (types != null && !types.isEmpty()) {
            types.forEach(type -> {
                Type one = typeService.getOne(new LambdaQueryWrapper<Type>().eq(Type::getName, type));
                int typeId;
                if (one == null) {
                    // 创建新标签
                    Type newType = new Type();
                    newType.setName(type);
                    typeService.save(newType);
                    typeId = newType.getId();
                } else {
                    typeId = one.getId();
                }
                // 存储联系
                VideoType videoType = new VideoType();
                videoType.setVideoId(video.getId());
                videoType.setTypeId(typeId);
                videoTypeService.save(videoType);
            });
        }
        return true;
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void transcodeVideo(Video video, String resolution) {
        String src = video.getSrc(); // 视频原始路径
        User user = userService.getById(video.getUserId());

        // 检查目录是否已经存在
        Path path = Paths.get(VIDEO_RESOLUTION_URL + user.getAccount());
        if (Files.notExists(path)) {
            try {
                // 创建目录
                Files.createDirectories(path); // 创建多层目录
                System.out.println("目录创建成功!");
            } catch (IOException e) {
                System.out.println("目录创建失败: " + e.getMessage());
            }
        } else {
            System.out.println("目录已存在!");
        }

        String outputPath = VIDEO_RESOLUTION_URL + user.getAccount() + "/" + video.getTitle() + "_" + resolution + ".m3u8"; // 生成 HLS 输出路径
        String databasePath = VIDEO_RESOLUTION_DATABASE_URL + user.getAccount() + "/"+ video.getTitle() + "_" + resolution + ".m3u8"; // 生成 HLS 输出路径
        String waterMark = user.getWaterMark();
        try {
            System.out.println("开始转码: " + src + " -> " + resolution);
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ffmpeg", "-i", src,
                    "-i", waterMark, // 添加水印
                    "-filter_complex", "overlay=10:10",
                    "-c:v", "libx264", "-b:v", getBitrate(resolution), "-preset", "fast", // 码率 & 质量
                    "-hls_time", "10", "-hls_list_size", "0", "-f", "hls", outputPath
            );

            processBuilder.inheritIO();
            Process process = processBuilder.start();
            process.waitFor();

            System.out.println("转码完成: " + outputPath);

            // 更新数据库，存储转码后的视频路径
            VideoResolution videoresolution = new VideoResolution();
            videoresolution.setVideoId(video.getId());
            videoresolution.setSrc(databasePath);
            videoresolution.setType(getType(resolution));

            videoresolutionService.save(videoresolution);
        } catch (Exception e) {
            System.out.println("转码失败: " + video.getTitle() + " -> " + resolution + e.getMessage());
        }
    }

    // 获取不同分辨率的码率
    private String getBitrate(String resolution) {
        switch (resolution) {
            case "720p": return "2M"; // 2 Mbps
            case "360p": return "800k"; // 800 kbps
            default: return "5M"; // 原始码率
        }
    }
    // 获取不同分辨率的码率
    private int getType(String resolution) {
        switch (resolution) {
            case "720p": return 1; // 2 Mbps
            case "360p": return 2; // 800 kbps
            default: return 0; // 原始码率
        }
    }

}




