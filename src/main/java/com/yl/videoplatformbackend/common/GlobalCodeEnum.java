package com.yl.videoplatformbackend.common;

import lombok.Getter;

@Getter
public enum GlobalCodeEnum {
    EX_SYSTEM(50000,"操作失败"),
    EX_PARAMS(40000,"参数异常"),
    SUCCESS(20000,"操作成功");

    private final int code;
    private final String msg;

    GlobalCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
