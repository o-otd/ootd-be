package com.ootd.be.api.my;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("my")
@RequiredArgsConstructor
public class MyController {

    private final MyService myService;



}
