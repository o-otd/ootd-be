package com.ootd.be.util.web;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import com.ootd.be.entity.Member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpringWebUtil {

    public static HttpServletResponse getHttpServletResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes)requestAttributes).getResponse();
    }

    public static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static Cookie getCookieByName(String name) {
        return WebUtils.getCookie(getHttpServletRequest(), name);
    }

}
