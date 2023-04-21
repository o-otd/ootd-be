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

}
