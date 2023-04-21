package com.ootd.be.api.confirm;

import com.ootd.be.api.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("confirm")
@RequiredArgsConstructor
public class ConfirmController {

    private final ConfirmService confirmService;

    @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
    @PostMapping(value = "register", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ApiResponse register(ConfirmDto.RegisterReq req) {

        confirmService.registerConfirm(req);

        return ApiResponse.ok();
    }

    @PostMapping(value = "vote")
    public ApiResponse vote(ConfirmDto.VoteReq req) {
        confirmService.vote(req);
        return ApiResponse.ok();
    }

    @PostMapping(value = "comment/register")
    public ApiResponse registerComment(ConfirmDto.RegisterCommentReq req) {
        confirmService.registerComment(req);
        return ApiResponse.ok();
    }

    @PostMapping(value = "comment/modify")
    public ApiResponse modifyComment(ConfirmDto.ModifyCommentReq req) {
        confirmService.modifyComment(req);
        return ApiResponse.ok();
    }

    @PostMapping(value = "comment/delete")
    public ApiResponse modifyComment(ConfirmDto.DeleteCommentReq req) {
        confirmService.deleteComment(req);
        return ApiResponse.ok();
    }

}
