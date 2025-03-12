package com.yl.videoplatformbackend.mapper;

import com.yl.videoplatformbackend.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Leon
* @description 针对表【video】的数据库操作Mapper
* @createDate 2025-02-21 16:46:03
* @Entity com.yl.videoplatformbackend.entity.Video
*/
@Mapper
public interface VideoMapper extends BaseMapper<Video> {

}




