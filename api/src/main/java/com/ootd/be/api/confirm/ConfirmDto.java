package com.ootd.be.api.confirm;

import com.ootd.be.entity.ConfirmVoteType;
import com.ootd.be.entity.Member;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ConfirmDto {

    @Data
    public static class PageReq {
        private int size = 10;
        private int page = 1;
    }

    @Data
    public static class ListReq {
        private PageReq pageReq;
    }

    @Data
    public static class PageRes {
        private int size;
        private int page;
        private int total;

        public static PageRes of(int size, int page, int total) {
            PageRes vo = new PageRes();
            vo.size = size;
            vo.page = page;
            vo.total = total;
            return vo;
        }
    }

    @Data
    public static class UserData {
        private Long id;
        private String name;
        private String avatar;

        public static UserData from(Member member) {
            UserData vo = new UserData();
            vo.id = member.getId();
            vo.name = member.getName();
            vo.avatar = member.getAvatar();
            return vo;
        }
    }

    @Data
    public static class ContentData {
        private Long id;

        private UserData user;

        private String startDate;
        private String endDate;
        private Long remains;

        private String content;

        private List<String> images;
    }

    @Data
    public static class ListRes {
        private PageRes page;
        private List<ContentData> datas;
    }

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
