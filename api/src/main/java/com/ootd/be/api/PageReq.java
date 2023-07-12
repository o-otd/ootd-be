package com.ootd.be.api;

import com.ootd.be.common.Variables;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Data
public class PageReq {
    private int size = Variables.Page.SIZE;
    private int page = Variables.Page.PAGE;

    public PageRequest toPageRequest(Sort sort) {
        return PageRequest.of(page, size, sort);
    }

    public PageRequest toPageRequest() {
        return PageRequest.of(page, size);
    }
}
