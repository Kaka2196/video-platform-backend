package com.yl.videoplatformbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.videoplatformbackend.entity.Type;
import com.yl.videoplatformbackend.service.TypeService;
import com.yl.videoplatformbackend.mapper.TypeMapper;
import org.springframework.stereotype.Service;

/**
* @author Leon
* @description 针对表【type】的数据库操作Service实现
* @createDate 2025-03-11 12:26:08
*/
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>
    implements TypeService{

}




