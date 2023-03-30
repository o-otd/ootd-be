package com.ootd.be.mapi;

public enum Category1DepthCode {
    outer("002"), top("001"), onepiece("020"), bottom("003"), skirt("022"), shoes("005"), bag("004"), womenBag("054"), sni("018");
    public final String code;

    Category1DepthCode(String code) {
        this.code = code;
    }
}
