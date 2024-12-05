package com.yl.videoplatformbackend.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class R<T> {
    private int code; // 状态码，比如200表示成功，其他自定义的状态码表示不同类型的错误等
    private String msg; // 提示消息，用于描述请求的处理结果情况
    private T data; // 泛型数据，用于存放实际要返回的业务数据，可以是对象、集合等各种类型

    // 全参构造函数
    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功时常用的静态方法，方便快速创建成功的返回结果实例
    public static <T> R<T> success(T data) {
        return new R<>(GlobalCodeEnum.SUCCESS.getCode(), GlobalCodeEnum.SUCCESS.getMsg(), data);
    }
    public static <T> R<T> success() {
        return new R<>(GlobalCodeEnum.SUCCESS.getCode(), GlobalCodeEnum.SUCCESS.getMsg(), null);
    }

    // 失败时常用的静态方法，根据传入的错误消息创建失败的返回结果实例
    public static <T> R<T> fail(GlobalCodeEnum codeEnum) {
        return new R<>(codeEnum.getCode(), codeEnum.getMsg(), null);
    }
    public static <T> R<T> fail(GlobalCodeEnum codeEnum, String msg) {
        return new R<>(codeEnum.getCode(), msg, null);
    }
    public static <T> R<T> fail(String msg) {
        return new R<>(GlobalCodeEnum.EX_SYSTEM.getCode(), msg, null);
    }
    public static <T> R<T> fail() {
        return new R<>(GlobalCodeEnum.EX_SYSTEM.getCode(), GlobalCodeEnum.EX_SYSTEM.getMsg(), null);
    }
    public static <T> R<T> fail(String msg,int code) {
        return new R<>(code, msg, null);
    }
}
