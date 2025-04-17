package com.yl.videoplatformbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yl.videoplatformbackend.entity.UserHistory;
import com.yl.videoplatformbackend.service.UserHistoryService;
import com.yl.videoplatformbackend.mapper.UserHistoryMapper;
import org.springframework.stereotype.Service;

/**
* @author Leon
* @description 针对表【userHistory】的数据库操作Service实现
* @createDate 2025-04-17 10:10:04
*/
@Service
public class UserHistoryServiceImpl extends ServiceImpl<UserHistoryMapper, UserHistory>
    implements UserHistoryService{

}




