package com.yl.videoplatformbackend.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    public ResponseEntity<R<?>> handleGlobalException(GlobalException e) {
        return ResponseEntity.status(500).body(R.fail(e.getMessage(),e.getCode()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<R<?>> handleOtherExceptions(Exception e) {
        return ResponseEntity.status(500).body(R.fail(e.getMessage()));
    }
}