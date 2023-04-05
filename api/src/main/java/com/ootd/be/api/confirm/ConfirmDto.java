package com.ootd.be.api.confirm;

import com.ootd.be.entity.ConfirmVoteType;
import lombok.Data;

public class ConfirmDto {

    @Data
    public static class RegisterReq {

        private String content;

        private String startDate;
        private String endDate;

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
