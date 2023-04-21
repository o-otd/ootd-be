package com.ootd.be.api.confirm;

import com.ootd.be.entity.ConfirmVoteType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ConfirmDto {

    @Data
    public static class RegisterReq {

        private String content;

        private String startDate;
        private String endDate;

        private String email;
        private String password;

        private List<MultipartFile> images;

    }

    @Data
    public static class VoteReq {

        private Long confirmId;
        private ConfirmVoteType voteType;

    }

    @Data
    public static class RegisterCommentReq {
        private Long confirmId;
        private Long parentCommentId;
        private String content;
    }

    @Data
    public static class ModifyCommentReq {
        private Long commentId;
        private String content;
    }

    @Data
    public static class DeleteCommentReq {
        private Long commentId;
    }

}
