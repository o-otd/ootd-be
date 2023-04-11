package com.ootd.be.api.confirm;

import com.ootd.be.api.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("confirm")
public class ConfirmController {

    @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
    @PostMapping(value = "register", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ApiResponse register(ConfirmDto.RegisterReq req) {

        log.info("{}", req);
//        log.info("{}", image);

        log.info("{}", req.getImage().getName());
        log.info("{}", req.getImage().getOriginalFilename());

        return ApiResponse.ok();
    }

}
