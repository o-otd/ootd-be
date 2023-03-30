package com.ootd.be.api.feed;

import java.util.List;

import lombok.Data;

public class FeedDto {

    @Data
    public static class RegisterReq {

        private String title;
        private String content;

        private FilterReq filter;

        private List<ItemReq> outers;
        private List<ItemReq> tops;
        private List<ItemReq> bottoms;
        private List<ItemReq> shoes;
        private List<ItemReq> bags;

        private List<String> hashtags;

    }

    @Data
    public static class FilterReq {
        private String gender;
        private String style;
        private String ageband;
        private List<String> colors;
        private List<String> height;
        private List<String> weight;
    }

    @Data
    public static class ItemReq {
        private Long goodsNo;
        private String goodsName;
        private String brandName;

        private String imageUrl;
    }
}
