package com.ootd.be.config.web;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ootd.be.api.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { Throwable.class })
    protected ApiResponse handleException(Throwable e) {
        logger.error(e);

        if (log.isDebugEnabled()) {
            e.printStackTrace();
        }

        if (e.getCause() == null) {
            return ApiResponse.fail("{}({})", e.getClass().getSimpleName(), e.getMessage());
        } else {
            return ApiResponse.fail("{}({})", e.getCause().getClass().getSimpleName(), e.getMessage());
        }
    }

}
