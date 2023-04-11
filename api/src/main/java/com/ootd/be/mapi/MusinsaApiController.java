package com.ootd.be.mapi;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ootd.be.api.ApiResponse;
import com.ootd.be.mapi.MusinsaDto.Goods;
import com.ootd.be.mapi.MusinsaDto.SearchReq;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("m-api")
@RequiredArgsConstructor
public class MusinsaApiController {

    private final MusinsaApiService mApiService;

    @PostMapping("search")
    public ApiResponse<List<Goods>> search(SearchReq data) {
        return ApiResponse.ok(mApiService.search(data.getKeyword(), data.getCategory()));
    }

}
