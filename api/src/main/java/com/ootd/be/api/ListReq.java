package com.ootd.be.api;

import lombok.Data;

@Data
public class ListReq {
    private PageReq page;

    public PageReq getPage() {
        return page == null ? new PageReq() : page;
    }
}
