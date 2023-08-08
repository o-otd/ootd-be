package com.ootd.be.api.confirm;

import com.ootd.be.api.PageReq;
import com.ootd.be.entity.*;
import com.ootd.be.util.CollectionUtils;
import com.ootd.be.util.DateTimeUtil;
import com.ootd.be.util.StringUtil;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ConfirmDto {

    @Data
    public static class VoteResult {
        private Long voteTypeId;
        private String wording;
        private int order;
        private int count;

        public static VoteResult from(ConfirmVoteType voteType) {
            VoteResult vo = new VoteResult();
            vo.voteTypeId = voteType.getId();
            vo.wording = voteType.getWording();
            vo.order = voteType.getOrder();
            vo.count = CollectionUtils.size(voteType.getConfirmVotes());
            return vo;
        }
    }

    @Data
    public static class ConfirmData {
        private Long id;

        private UserData user;

        private String startDate;
        private String endDate;
        private Long remains;

        private String content;

        private List<String> images;

        private Long myVoting;

        private List<VoteResult> votes;

        private int totalComments;

        private CommentData bestComment;

        public static ConfirmData from(Confirm confirm) {
            ConfirmData vo = new ConfirmData();
            vo.setId(confirm.getId());
            vo.setUser(ConfirmDto.UserData.from(confirm.getMember()));
            vo.setStartDate(confirm.getStartDate());
            vo.setEndDate(confirm.getEndDate());

            LocalDateTime endDateTime = DateTimeUtil.YMD.from(confirm.getEndDate(), true);
            long remains = DateTimeUtil.dayDiff(LocalDateTime.now(), endDateTime);
            vo.setRemains(remains);

            vo.setContent(confirm.getContent());

            vo.setImages(confirm.getImages().stream().map(ConfirmImage::getImagePath).collect(Collectors.toList()));

            vo.setTotalComments(CollectionUtils.size(confirm.getComments()));

            vo.votes = confirm.getVoteTypes().stream().map(VoteResult::from).collect(Collectors.toList());

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

        protected String registered;

        protected int like;
        protected long nested;


        public static CommentData from(ConfirmComment comment) {
            if (comment == null) return null;
            CommentData vo = new CommentData();
            vo.setId(comment.getId());
            vo.setUser(UserData.from(comment.getMember()));
            vo.setComment(comment.getContent());
            vo.setLike(CollectionUtils.size(comment.getLikes()));
            vo.setRegistered(DateTimeUtil.YMDHM.format(comment.getCreatedAt()));
            return vo;
        }

        @Override
        public String toString() {
            return StringUtil.format("{} : {} ({} | {} | {})", this.user, this.comment, this.myComment, this.myLike, this.like);
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
            vo.setRegistered(DateTimeUtil.YMDHM.format(comment.getCreatedAt()));

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

        private List<VoteTypeReq> voteTypeReqs;

        private List<MultipartFile> images;

    }

    @Data
    public static class VoteTypeReq {
        private String wording;
        private int order;
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
        private Long voteTypeId;
    }

    @Data
    public static class VoteCancelReq {
        private Long confirmId;
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
        private CommentData comment;

        public static RegisterCommentRes of(ConfirmComment comment) {
            RegisterCommentRes vo = new RegisterCommentRes();
            vo.comment = CommentData.from(comment);
            vo.comment.myComment = true;
            vo.comment.myLike = false;
            vo.comment.like = 0;
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
