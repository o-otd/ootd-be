package com.ootd.be.config.web;

import com.ootd.be.api.ApiResponse;
import com.ootd.be.config.security.exception.OotdAuthenticationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@Slf4j
@RestController
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler implements AccessDeniedHandler {

    @ExceptionHandler(value = {HttpStatusCodeException.class})
    public ApiResponse httpStatusCodeException(HttpStatusCodeException e) {
        log.error("[httpStatusCode] {}", e.getMessage());
        if (e.getStatusCode().is4xxClientError()) {
            return ApiResponse.fail(400, "인증 오류");
        } else {
            return ApiResponse.fail(500, e.getMessage());
        }
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ApiResponse authenticationException(AuthenticationException e) {
        log.error("[authentication] {}", e.getMessage());
        return ApiResponse.fail(400, "인증 오류");
    }

    @ExceptionHandler(value = {Exception.class})
    public ApiResponse exception(Exception e) {
        log.error("[exception] {}", e.getMessage());
        return ApiResponse.fail(500, e.getMessage());
    }

    @ExceptionHandler(value = {Throwable.class})
    public ApiResponse throwable(Throwable e) {
        log.error("[throwable] {}", e.getMessage());
        return ApiResponse.fail(500, e.getMessage());
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        throw new OotdAuthenticationException(accessDeniedException.getMessage());
    }

}
