package com.yl.videoplatformbackend.controller;

import com.yl.videoplatformbackend.common.R;
import com.yl.videoplatformbackend.entity.Type;
import com.yl.videoplatformbackend.service.TypeService;
import com.yl.videoplatformbackend.service.VideoTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/videoType")
public class VideoTypeController {
    @Autowired
    private TypeService typeService;
    @GetMapping("/list")
    public R<List<Type>> list() {
        return R.success(typeService.list());
    }
}
