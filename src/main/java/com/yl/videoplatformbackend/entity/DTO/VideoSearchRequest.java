package com.yl.videoplatformbackend.entity.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VideoSearchRequest {

    private String keyword;

    private String type;
}
