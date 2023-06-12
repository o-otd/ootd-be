package com.ootd.be.api.confirm;

import com.ootd.be.api.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ootd.be.api.confirm.ConfirmDto.*;

@Slf4j
@RestController
@RequestMapping("confirm")
@RequiredArgsConstructor
public class ConfirmController {

    private final ConfirmService confirmService;

    @PostMapping(value = "list")
    public ApiResponse list(ListReq req) {
        return ApiResponse.ok(confirmService.confirms(req));
    }

    @Secured({"USER"})
    @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
    @PostMapping(value = "register", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ApiResponse<RegisterRes> register(RegisterReq req) {
        return ApiResponse.ok(confirmService.registerConfirm(req));
    }

    @PostMapping(value = "vote")
    public ApiResponse vote(VoteReq req) {
        confirmService.vote(req);
        return ApiResponse.ok();
    }

    @PostMapping(value = "comment/list")
    public ApiResponse<ConfirmDto.ListRes<ConfirmDto.CommentData>> commentList(CommentListReq req) {
        return ApiResponse.ok(confirmService.comments(req));
    }

    @PostMapping(value = "nestedComment/list")
    public ApiResponse<ConfirmDto.ListRes<ConfirmDto.NestedCommentData>> nestedCommentList(CommentListReq req) {
        return ApiResponse.ok(confirmService.nestedComments(req));
    }

    @PostMapping(value = "comment/register")
    public ApiResponse<RegisterCommentRes> registerComment(RegisterCommentReq req) {
        return ApiResponse.ok(confirmService.registerComment(req));
    }

    @PostMapping(value = "comment/modify")
    public ApiResponse modifyComment(ModifyCommentReq req) {
        confirmService.modifyComment(req);
        return ApiResponse.ok();
    }

    @PostMapping(value = "comment/delete")
    public ApiResponse deleteComment(DeleteCommentReq req) {
        confirmService.deleteComment(req);
        return ApiResponse.ok();
    }

    @PostMapping(value = "comment/like")
    public ApiResponse likeComment(LikeCommentReq req) {
        confirmService.likeComment(req);
        return ApiResponse.ok();
    }

    @PostMapping(value = "comment/dislike")
    public ApiResponse dislikeComment(LikeCommentReq req) {
        confirmService.dislikeComment(req);
        return ApiResponse.ok();
    }


}
