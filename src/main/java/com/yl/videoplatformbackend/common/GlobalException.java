package com.yl.videoplatformbackend.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalException extends RuntimeException {
    private int code;
    private String message;

    public GlobalException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    public GlobalException(GlobalCodeEnum codeEnum) {
        super(codeEnum.getMsg());
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMsg();
    }
    public GlobalException(GlobalCodeEnum codeEnum, String message) {
        super(message);
        this.code = codeEnum.getCode();
        this.message = message;
    }

}