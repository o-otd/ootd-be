package com.ootd.be.api.feed;

import com.ootd.be.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    // fixme. 파일 테스트 및 수정
    @PostMapping("register")
    public ApiResponse register(FeedDto.RegisterReq req) {
        feedService.registerFeed(req);
        return ApiResponse.ok();
    }

    @PostMapping("like")
    public ApiResponse like(Long feedId) {
        feedService.like(feedId);
        return ApiResponse.ok();
    }

    @PostMapping("dislike")
    public ApiResponse dislike(Long feedId) {
        feedService.dislike(feedId);
        return ApiResponse.ok();
    }

    @PostMapping("bookmark/add")
    public ApiResponse addBookmark(Long feedId) {
        feedService.addBookmark(feedId);
        return ApiResponse.ok();
    }

    @PostMapping("bookmark/delete")
    public ApiResponse deleteBookmark(Long feedId) {
        feedService.deleteBookmark(feedId);
        return ApiResponse.ok();
    }

    @PostMapping("follow")
    public ApiResponse follow(Long feedId) {
        feedService.follow(feedId);
        return ApiResponse.ok();
    }

    @PostMapping("unFollow")
    public ApiResponse unFollow(Long feedId) {
        feedService.unFollow(feedId);
        return ApiResponse.ok();
    }

}
