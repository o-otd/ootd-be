package com.ootd.be.mapi;

import java.util.PrimitiveIterator;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ootd.be.api.ApiResponse;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("m-api")
@RequiredArgsConstructor
public class MusinsaApiController {

    private final MusinsaApiService mApiService;

    @Data
    public static class MusinsaSearchReqDto {
        private String keyword;
    }

    @PostMapping("search")
    public ApiResponse<MusinsaApiRes> search(@RequestBody MusinsaSearchReqDto data) {
        return ApiResponse.ok(mApiService.search(data.getKeyword()));
    }

}
