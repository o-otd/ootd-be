package com.ootd.be.api.my;

import com.ootd.be.entity.Feed;
import lombok.Data;

public class MyDto {

    @Data
    public static class MyPageResDto {
        private Feed feed;
        private long followerCnt;
        private long followeeCnt;
        private long likeCnt;
        private long bookmarkCnt;
        private long confirmCnt;
        private long commentCnt;
    }

    public enum ArticleCategory {
        feed, confirm, product, ;
    }

    @Data
    public static class LikeListResDto {

        private Long id;
        private ArticleCategory category;
        private String title;
        private String imageUrl;

    }

}
