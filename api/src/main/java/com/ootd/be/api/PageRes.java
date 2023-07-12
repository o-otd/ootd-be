package com.ootd.be.api;

import com.ootd.be.api.confirm.ConfirmDto;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PageRes {
    private int size;
    private int page;
    private int total;

    public static PageRes of(Page page) {
        PageRes vo = new PageRes();
        vo.size = page.getSize();
        vo.page = page.getNumber();
        vo.total = page.getTotalPages();
        return vo;
    }
}
