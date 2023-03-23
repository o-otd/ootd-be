package com.ootd.be.mapi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MusinsaApiServiceTest {

    private final MusinsaApiService service = new MusinsaApiService();

    @Test
    public void search() {

        service.search("바람막이");

    }

}