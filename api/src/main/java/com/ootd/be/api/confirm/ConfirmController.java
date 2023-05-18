package com.ootd.be.api.confirm;

import com.ootd.be.api.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("confirm")
@RequiredArgsConstructor
public class ConfirmController {

    private final ConfirmService confirmService;

    @PostMapping(value = "list")
    public ApiResponse list(ConfirmDto.ListReq req) {
        return ApiResponse.ok(confirmService.comfirms(req));
    }

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

    @PostMapping(value = "comment/list")
    public ApiResponse commentList(ConfirmDto.CommentListReq req) {
        return ApiResponse.ok(confirmService.comments(req));
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
    public ApiResponse deleteComment(ConfirmDto.DeleteCommentReq req) {
        confirmService.deleteComment(req);
        return ApiResponse.ok();
    }

    @PostMapping(value = "comment/like")
    public ApiResponse likeComment(ConfirmDto.LikeCommentReq req) {
        confirmService.likeComment(req);
        return ApiResponse.ok();
    }

    @PostMapping(value = "comment/dislike")
    public ApiResponse dislikeComment(ConfirmDto.LikeCommentReq req) {
        confirmService.dislikeComment(req);
        return ApiResponse.ok();
    }


}
