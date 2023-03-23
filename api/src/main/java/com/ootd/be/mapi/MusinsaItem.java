package com.ootd.be.mapi;

import lombok.Data;

@Data
public class MusinsaItem {

    private Long goodsNo;
    private String goodsName;

    private Long couponPrice;
    private Long price;
    private Long normalPrice;
    private String brandName;

    private String imageUrl;
    private String linkUrl;

}
