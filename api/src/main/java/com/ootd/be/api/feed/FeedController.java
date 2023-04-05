package com.ootd.be.api.feed;

import com.ootd.be.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    // fixme. 파일 테스트 및 수정
    @PostMapping("register")
    public ApiResponse register(@RequestBody FeedDto.RegisterReq req, MultipartFile mainImage) {
        feedService.registerFeed(req, mainImage);
        return ApiResponse.ok();
    }

    @PostMapping("like")
    public ApiResponse like(@RequestBody Long feedId) {
        feedService.like(feedId);
        return ApiResponse.ok();
    }

    @PostMapping("dislike")
    public ApiResponse dislike(@RequestBody Long feedId) {
        feedService.dislike(feedId);
        return ApiResponse.ok();
    }

    @PostMapping("bookmark/add")
    public ApiResponse addBookmark(@RequestBody Long feedId) {
        feedService.addBookmark(feedId);
        return ApiResponse.ok();
    }

    @PostMapping("bookmark/delete")
    public ApiResponse deleteBookmark(@RequestBody Long feedId) {
        feedService.deleteBookmark(feedId);
        return ApiResponse.ok();
    }

    @PostMapping("follow")
    public ApiResponse follow(@RequestBody Long feedId) {
        feedService.follow(feedId);
        return ApiResponse.ok();
    }

    @PostMapping("unFollow")
    public ApiResponse unFollow(@RequestBody Long feedId) {
        feedService.unFollow(feedId);
        return ApiResponse.ok();
    }

}
