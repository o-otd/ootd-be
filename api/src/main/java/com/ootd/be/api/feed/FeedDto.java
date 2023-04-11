package com.ootd.be.api.feed;

import java.util.List;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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

        private MultipartFile mainImage;

    }

    @Data
    public static class FilterReq {
        private GenderFilter gender;
        private StyleFilter style;
        private int age;
        private List<String> colors;
        private RangeFilter height;
        private RangeFilter weight;

        public enum GenderFilter {
            male, female;
        }

        public enum StyleFilter {
            casual, modern, street, chic, romantic, dandy, sports, girlish, formal, american, retro;
        }

        @Data
        public static class RangeFilter {
            private int min;
            private int max;
        }

    }

    @Data
    public static class ItemReq {
        private Long goodsNo;
        private String goodsName;
        private String brandName;

        private String imageUrl;
    }
}
