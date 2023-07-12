package com.ootd.be.api;

import com.ootd.be.api.confirm.ConfirmDto;
import lombok.Data;

import java.util.List;

@Data
public class ListRes<T> {
    private PageRes page;
    private List<T> datas;

    public static <T> ListRes<T> of(PageRes page, List<T> datas) {
        ListRes<T> vo = new ListRes();
        vo.page = page;
        vo.datas = datas;
        return vo;
    }
}
