package com.ootd.be.api.confirm;

import com.ootd.be.api.auth.AuthController;
import com.ootd.be.api.auth.AuthService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class ConfirmServiceTest {

    @Resource
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Resource
    private AuthService authService;

    @Resource
    private ConfirmService confirmService;

    @BeforeEach
    public void createAuthenticate() {

        AuthController.JoinReqDto req = new AuthController.JoinReqDto();
        req.setEmail("aa@aa.com");
        req.setPassword("123");
        req.setName("aaa");
        authService.join(req);

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
    }

    @Test
    public void registerConfirm() {
        ConfirmDto.RegisterReq req = new ConfirmDto.RegisterReq();

        req.setContent("이거 어때요?");
        req.setStartDate("20230501");
        req.setEndDate("20230531");

        List<MultipartFile> images = null;
        try {
            images = new ArrayList<>();
            MultipartFile sampleFile = new MockMultipartFile("사진1.png", "사진1.png", null, new FileInputStream(new File(System.getProperty("user.home") + "/Documents/sample1.png")));
            images.add(sampleFile);
            MultipartFile sampleFile2 = new MockMultipartFile("사진2.png", "사진2.png", null, new FileInputStream(new File(System.getProperty("user.home") + "/Documents/sample2.png")));
            images.add(sampleFile2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        req.setImages(images);

        confirmService.registerConfirm(req);
    }

}