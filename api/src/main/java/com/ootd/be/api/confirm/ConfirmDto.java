package com.ootd.be.api.confirm;

import com.ootd.be.common.Variables;
import com.ootd.be.entity.*;
import com.ootd.be.util.DateTimeUtil;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class ConfirmDto {

    @Data
    public static class ListReq {
        private PageReq page;
    }

    @Data
    public static class ListRes<T> {
        private PageRes page;
        private List<T> datas;

        public static <T> ListRes<T> of(PageRes page, List<T> datas) {
            ListRes<T> vo = new ListRes();
            vo.page = page;
            vo.datas = datas;
            return vo;
        }
    }

//    @Data
//    public static class ListRes {
//        private PageRes page;
//        private List<ConfirmData> datas;
//
//        public static ListRes of(PageRes page, List<ConfirmData> datas) {
//            ListRes vo = new ListRes();
//            vo.page = page;
//            vo.datas = datas;
//            return vo;
//        }
//    }

    @Data
    public static class ConfirmData {
        private Long id;

        private UserData user;

        private String startDate;
        private String endDate;
        private Long remains;

        private String content;

        private List<String> images;

        private ConfirmVoteType myVoting;

        private Long goodCnt;
        private Long badCnt;

        private CommentData bestComment;

        public static ConfirmData from(Confirm confirm) {
            ConfirmData vo = new ConfirmData();
            vo.setId(confirm.getId());
            vo.setUser(ConfirmDto.UserData.from(confirm.getMember()));
            vo.setStartDate(confirm.getStartDate());
            vo.setEndDate(confirm.getEndDate());

            LocalDateTime endDateTime = DateTimeUtil.FORMATTER.YMD.from(confirm.getEndDate());
            long remains = ChronoUnit.DAYS.between(LocalDateTime.now(), endDateTime);
            vo.setRemains(remains);

            vo.setContent(confirm.getContent());

            vo.setImages(confirm.getImages().stream().map(ConfirmImage::getImagePath).collect(Collectors.toList()));

            return vo;
        }
    }

    @Data
    public static class PageReq {
        private int size = Variables.Page.SIZE;
        private int page = Variables.Page.PAGE;

        public PageRequest toPageRequest(Sort sort) {
            return PageRequest.of(page, size, sort);
        }
    }

    @Data
    public static class PageRes {
        private int size;
        private int page;
        private int total;

        public static PageRes of(Page page) {
            PageRes vo = new PageRes();
            vo.size = page.getSize();
            vo.page = page.getNumber();
            vo.total = page.getTotalPages();
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
    public static class CommentData {
        private Long id;
        private UserData user;
        private String comment;

        private boolean myComment;
        private boolean myLike;

        private int like;

        public static CommentData from(ConfirmComment comment) {
            if (comment == null) return null;
            CommentData vo = new CommentData();
            vo.setId(comment.getId());
            vo.setUser(UserData.from(comment.getMember()));
            vo.setComment(comment.getContent());
            vo.setLike(comment.getLikes().size());

            return vo;
        }
    }

    @Data
    public static class RegisterReq {

        private String content;

        private String startDate;
        private String endDate;

        private List<MultipartFile> images;

    }

    @Data
    public static class VoteReq {

        private Long confirmId;
        private ConfirmVoteType voteType;

    }

    @Data
    public static class CommentListReq {
        private Long confirmId;
        private PageReq page;
    }

    @Data
    public static class LikeCommentReq {
        private Long commentId;
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
