package com.ootd.be.mapi;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;
import lombok.RequiredArgsConstructor;

public class MusinsaDto {

    public enum Category {
        outer(Category1DepthCode.outer)
        , top(Category1DepthCode.top, Category1DepthCode.onepiece)
        , bottom(Category1DepthCode.bottom, Category1DepthCode.onepiece, Category1DepthCode.skirt)
        , shoes(Category1DepthCode.shoes, Category1DepthCode.sni)
        , bag(Category1DepthCode.bag, Category1DepthCode.womenBag)
        ;

        public final List<Category1DepthCode> categoryCodes;

        Category(Category1DepthCode...codes) {
            this.categoryCodes = List.of(codes);
        }
    }

    @RequiredArgsConstructor
    public enum DefaultParameter {
        siteKindId("musinsa")
        , sex("A")
        , viewType("small")
        , size("50")
        , includeSoldOut("true")
        , page("1")
        , sort("POPULAR")
        , originalYn("N")
        , incldueUnisex("true")
        ;
        final String defaultValue;

        public static final Consumer<UriComponentsBuilder> toParameters = (builder) -> {
            Arrays.stream(values()).forEach(param -> builder.queryParam(param.name(), param.defaultValue));
        };
    }

    @Data
    public static class SearchReq {
        private String keyword;
        private Category category;
    }

    @Data
    public static class Goods {
        private Long goodsNo;
        private String goodsName;

        private Long couponPrice;
        private Long price;
        private Long normalPrice;

        private String brandName;

        private String imageUrl;
        private String linkUrl;
    }
}
