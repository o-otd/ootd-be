package com.ootd.be.api.my;

import com.ootd.be.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("my")
@RequiredArgsConstructor
public class MyController {

    private final MyService myService;

    @PostMapping("page")
    public ApiResponse<MyDto.MyPageResDto> page() {
        return ApiResponse.ok(myService.page());
    }

}
