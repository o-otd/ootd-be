package com.ootd.be.api.confirm;

import com.ootd.be.api.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("confirm")
public class ConfirmController {

    @PostMapping("register")
    public ApiResponse register(@RequestBody ConfirmDto.RegisterReq req, List<MultipartFile> images) {

        return ApiResponse.ok();
    }



}
