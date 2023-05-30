package com.ootd.be.api.confirm;

import com.ootd.be.common.Variables;
import com.ootd.be.entity.*;
import com.ootd.be.util.DateTimeUtil;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        private int goodCnt;
        private int badCnt;

        private CommentData bestComment;

        public static ConfirmData from(Confirm confirm) {
            ConfirmData vo = new ConfirmData();
            vo.setId(confirm.getId());
            vo.setUser(ConfirmDto.UserData.from(confirm.getMember()));
            vo.setStartDate(confirm.getStartDate());
            vo.setEndDate(confirm.getEndDate());

            LocalDateTime endDateTime = DateTimeUtil.YMD.from(confirm.getEndDate(), true);
            long remains = ChronoUnit.DAYS.between(LocalDateTime.now(), endDateTime);
            vo.setRemains(remains);

            vo.setContent(confirm.getContent());

            vo.setImages(confirm.getImages().stream().map(ConfirmImage::getImagePath).collect(Collectors.toList()));

            Map<ConfirmVoteType, List<ConfirmVote>> voteTypes = confirm.getVotes().stream().collect(Collectors.groupingBy(v -> v.getVoteType()));
            vo.setGoodCnt(voteTypes.getOrDefault(ConfirmVoteType.good, new ArrayList<> ()).size());
            vo.setBadCnt(voteTypes.getOrDefault(ConfirmVoteType.bad, new ArrayList<> ()).size());

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

        public PageRequest toPageRequest() {
            return PageRequest.of(page, size);
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

        @Override
        public String toString() {
            return this.name + "(" + this.id + " : " + this.avatar + ")";
        }
    }


    @Data
    public static class CommentData {
        protected Long id;
        protected UserData user;
        protected String comment;

        protected boolean myComment;
        protected boolean myLike;

        protected int like;

        public static CommentData from(ConfirmComment comment) {
            if (comment == null) return null;
            CommentData vo = new CommentData();
            vo.setId(comment.getId());
            vo.setUser(UserData.from(comment.getMember()));
            vo.setComment(comment.getContent());
            vo.setLike(comment.getLikes().size());

            return vo;
        }

        @Override
        public String toString() {
            return MessageFormat.format("{} : {} ({} | {} | {})", this.user, this.comment, this.myComment, this.myLike, this.like);
        }
    }

    @Data
    public static class NestedCommentData extends CommentData {

        protected CommentData parentComment;

        public static NestedCommentData from(ConfirmComment comment) {
            if (comment == null) return null;
            NestedCommentData vo = new NestedCommentData();
            vo.setId(comment.getId());
            vo.setUser(UserData.from(comment.getMember()));
            vo.setComment(comment.getContent());
            vo.setLike(comment.getLikes().size());
            vo.setParentComment(CommentData.from(comment.getParentComment()));

            return vo;
        }

        @Override
        public String toString() {
            return MessageFormat.format("{} : {} ({} | {} | {})", this.user, this.comment, this.myComment, this.myLike, this.like);
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
    public static class RegisterRes {
        private Long id;

        public static RegisterRes of(Long id) {
            RegisterRes vo = new RegisterRes();
            vo.id = id;
            return vo;
        }
    }

    @Data
    public static class VoteReq {
        private Long confirmId;
        private ConfirmVoteType voteType;
    }

    @Data
    public static class CommentListReq {
        private Long targetId;
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
    public static class RegisterCommentRes {
        private Long commentId;

        public static RegisterCommentRes of(Long id) {
            RegisterCommentRes vo = new RegisterCommentRes();
            vo.commentId = id;
            return vo;
        }
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
