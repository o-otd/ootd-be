package com.ootd.be.config.web;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ootd.be.api.auth.ApiResponse;
import com.ootd.be.exception.ValidationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(value = { ValidationException.class })
//    protected ApiResponse handleException(Throwable e) {
//        logger.error(e);
//
//        if (log.isDebugEnabled()) {
//            e.printStackTrace();
//        }
//
//        return ApiResponse.ok();
//    }

}
