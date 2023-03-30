package com.ootd.be.mapi;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.ootd.be.AbstractTestBase;
import com.ootd.be.mapi.MusinsaDto.Category;
import com.ootd.be.mapi.MusinsaDto.Goods;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MusinsaApiServiceTest extends AbstractTestBase {

    @Resource
    private MusinsaApiService service;

    @Test
    public void search() {
        List<Goods> list = service.search("바람막이", Category.outer);
        log.info("size : {}", list.size());

        list = service.search("청바지", Category.bottom);
        log.info("size : {}", list.size());

        list = service.search("블레이저", Category.outer);
        log.info("size : {}", list.size());

    }

}