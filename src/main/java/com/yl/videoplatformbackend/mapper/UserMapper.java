package com.yl.videoplatformbackend.mapper;

import com.yl.videoplatformbackend.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author yl
 * @since 2024-11-28
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
