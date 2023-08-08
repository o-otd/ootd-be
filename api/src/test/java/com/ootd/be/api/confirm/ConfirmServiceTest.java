package com.ootd.be.api.confirm;

import com.ootd.be.api.ListReq;
import com.ootd.be.api.ListRes;
import com.ootd.be.api.PageReq;
import com.ootd.be.api.ServiceTestBase;
import com.ootd.be.exception.ValidationException;
import com.ootd.be.util.GridPrinter;
import com.ootd.be.util.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ConfirmServiceTest extends ServiceTestBase {

    @Resource
    private ConfirmService confirmService;

    private Long confirmId = 2L;
    private Long confirmVoteTypeId = 2L;

    @Test
    public void t01_registerConfirm() {
        ConfirmDto.RegisterReq req = new ConfirmDto.RegisterReq();

        String content = "이거 어때요?";
        String startDate = "20230501";
        String endDate = "20230531";

        req.setContent(content);
        req.setStartDate(startDate);
        req.setEndDate(endDate);

        List<MultipartFile> images = new ArrayList<>();
        try {
            MultipartFile sampleFile = new MockMultipartFile("사진1.png", "사진1.png", null, new FileInputStream(System.getProperty("user.home") + "/Documents/sample1.png"));
            images.add(sampleFile);
            MultipartFile sampleFile2 = new MockMultipartFile("사진2.png", "사진2.png", null, new FileInputStream(System.getProperty("user.home") + "/Documents/sample2.png"));
            images.add(sampleFile2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        req.setImages(images);

        ConfirmDto.VoteTypeReq req1 = new ConfirmDto.VoteTypeReq();
        req1.setOrder(1);
        String voting1 = "1111";
        req1.setWording(voting1);

        ConfirmDto.VoteTypeReq req2 = new ConfirmDto.VoteTypeReq();
        req2.setOrder(2);
        String voting2 = "2222";
        req2.setWording(voting2);

        req.setVoteTypeReqs(List.of(req1, req2));

        ConfirmDto.ConfirmData res = confirmService.registerConfirm(req);
        confirmId = res.getId();
        confirmVoteTypeId = res.getVotes().get(0).getVoteTypeId();

        // check.
        assertThat(res.getContent()).isEqualTo(content);
        assertThat(res.getStartDate()).isEqualTo(startDate);
        assertThat(res.getEndDate()).isEqualTo(endDate);

        assertThat(res.getImages().size()).isEqualTo(2);

        assertThat(res.getVotes().size()).isEqualTo(2);

        assertThat(res.getVotes().get(0).getWording()).isEqualTo(voting1);
        assertThat(res.getVotes().get(1).getWording()).isEqualTo(voting2);

    }

    @Test
    public void t02_confirms() {

        ListReq req = defaultListReq();

        final String voteFormat = "{}({}) : {}";
        final Function<ConfirmDto.VoteResult, String> voteFormatting = (voteResult) -> StringUtil.format(voteFormat, voteResult.getWording(), voteResult.getVoteTypeId(), voteResult.getCount());

        ListRes<ConfirmDto.ConfirmData> confirms = confirmService.confirms(req);
        GridPrinter printer = GridPrinter.of("아이디", "사용자", "기간", "내용", "이미지", "투표여부", "투표1", "투표2", "댓글");
        confirms.getDatas().forEach(confirm -> {
            printer.add(confirm.getId(), confirm.getUser(), confirm.getStartDate() + "-" + confirm.getEndDate() + "(" + confirm.getRemains() + ")"
                    , confirm.getContent(), confirm.getImages().stream().collect(Collectors.joining(","))
                    , confirm.getMyVoting(), voteFormatting.apply(confirm.getVotes().get(0)), voteFormatting.apply(confirm.getVotes().get(1)), confirm.getBestComment()
            );

        });

        printer.print();

    }

    private static ListReq defaultListReq() {
        ListReq req = new ListReq();
        req.setPage(new PageReq());
        return req;
    }

    @Test
    void t03_vote() {

        ConfirmDto.VoteReq req = new ConfirmDto.VoteReq();
        req.setConfirmId(confirmId);
        req.setVoteTypeId(confirmVoteTypeId);
        confirmService.vote(req);

        ListRes<ConfirmDto.ConfirmData> confirms = confirmService.confirms(defaultListReq());
        ConfirmDto.ConfirmData confirm = confirms.getDatas().get(0);

        assertThat(confirm.getMyVoting()).isEqualTo(confirmVoteTypeId);
        assertThat(confirm.getVotes().get(0).getCount()).isEqualTo(1);

    }

    @Test
    public void t04_confirms() {
        t02_confirms();
    }

    private Long commentId = 6L;

    @Test
    void t05_registerComment() {

        ConfirmDto.RegisterCommentReq req = new ConfirmDto.RegisterCommentReq();
        req.setConfirmId(confirmId);
        req.setContent("별룬뎅?");
        ConfirmDto.RegisterCommentRes res = confirmService.registerComment(req);
        commentId = res.getComment().getId();

        ListRes<ConfirmDto.ConfirmData> confirms = confirmService.confirms(defaultListReq());
        ConfirmDto.ConfirmData confirm = confirms.getDatas().get(0);

        assertThat(confirm.getBestComment().getId()).isEqualTo(commentId);
        assertThat(confirm.getTotalComments()).isEqualTo(1);

    }

    @Test
    void t06_comments() {

        ConfirmDto.CommentListReq req = getCommentListReq();

        GridPrinter printer = GridPrinter.of("아이디", "사용자", "내용", "좋아요", "내 댓글", "내 좋아요");

        ListRes<ConfirmDto.CommentData> comments = confirmService.comments(req);
        comments.getDatas().forEach(comment -> {
            printer.add(comment.getId(), comment.getUser(), comment.getComment(), comment.getLike(), comment.isMyComment(), comment.isMyLike());
        });

        printer.print();
    }

    @Test
    void t07_likeComment() {

        ConfirmDto.LikeCommentReq req = new ConfirmDto.LikeCommentReq();
        req.setCommentId(commentId);
        confirmService.likeComment(req);

        ListRes<ConfirmDto.ConfirmData> confirms = confirmService.confirms(defaultListReq());
        ConfirmDto.ConfirmData confirm = confirms.getDatas().get(0);

        assertThat(confirm.getBestComment().getId()).isEqualTo(commentId);
        assertThat(confirm.getBestComment().getLike()).isEqualTo(1);

        assertThat(confirm.getBestComment().isMyComment()).isTrue();

        assertThat(confirm.getBestComment().getNested()).isZero();

        ConfirmDto.CommentListReq commentListReq = getCommentListReq();
        ListRes<ConfirmDto.CommentData> comments = confirmService.comments(commentListReq);
        ConfirmDto.CommentData comment = comments.getDatas().get(0);

        assertThat(comment.getNested()).isZero();
        assertThat(comment.getLike()).isEqualTo(1);

    }

    private ConfirmDto.CommentListReq getCommentListReq() {
        ConfirmDto.CommentListReq commentListReq = new ConfirmDto.CommentListReq();
        commentListReq.setTargetId(confirmId);
        commentListReq.setPage(new PageReq());
        return commentListReq;
    }

    @Test
    void t08_comments() {
        t06_comments();
    }

    @Test
    void t09_dislikeComment() {

        ConfirmDto.LikeCommentReq req = new ConfirmDto.LikeCommentReq();
        req.setCommentId(commentId);
        confirmService.dislikeComment(req);

        ListRes<ConfirmDto.ConfirmData> confirms = confirmService.confirms(defaultListReq());
        ConfirmDto.ConfirmData confirm = confirms.getDatas().get(0);

        assertThat(confirm.getBestComment().getId()).isEqualTo(commentId);
        assertThat(confirm.getBestComment().getLike()).isZero();

        ConfirmDto.CommentListReq commentListReq = getCommentListReq();
        ListRes<ConfirmDto.CommentData> comments = confirmService.comments(commentListReq);
        ConfirmDto.CommentData comment = comments.getDatas().get(0);

        assertThat(comment.getNested()).isZero();
        assertThat(comment.getLike()).isZero();
    }

    @Test
    void t10_comments() {
        t06_comments();
    }

    @Test
    void t11_modifyComment() {

        ConfirmDto.ModifyCommentReq req = new ConfirmDto.ModifyCommentReq();
        req.setCommentId(commentId);
        String content = "so so";
        req.setContent(content);
        confirmService.modifyComment(req);

        ConfirmDto.CommentListReq commentListReq = getCommentListReq();
        ListRes<ConfirmDto.CommentData> comments = confirmService.comments(commentListReq);
        ConfirmDto.CommentData comment = comments.getDatas().get(0);

        assertThat(comment.getComment()).isEqualTo(content);
    }

    @Test
    void t12_comment() {
        t06_comments();
    }

    @Test
    void t13_deleteComment() {

        ConfirmDto.DeleteCommentReq req = new ConfirmDto.DeleteCommentReq();
        req.setCommentId(commentId);
        confirmService.deleteComment(req);

        ConfirmDto.CommentListReq commentListReq = getCommentListReq();
        ListRes<ConfirmDto.CommentData> comments = confirmService.comments(commentListReq);

        assertThat(comments.getDatas()).isEmpty();

    }

    @Test
    void t14_registerNestedComment_01() {

        ConfirmDto.RegisterCommentReq req = new ConfirmDto.RegisterCommentReq();
        req.setConfirmId(confirmId);
        req.setParentCommentId(commentId);
        req.setContent("ㅇㅈ");

        assertThatThrownBy(() -> confirmService.registerComment(req)).isInstanceOf(ValidationException.class);

    }


    @Test
    void t14_registerNestedComment_02() {

        ConfirmDto.RegisterCommentReq parentReq = new ConfirmDto.RegisterCommentReq();
        parentReq.setConfirmId(confirmId);
        parentReq.setContent("상위 댓글");
        ConfirmDto.RegisterCommentRes parent = confirmService.registerComment(parentReq);

        commentId = parent.getComment().getId();

        ConfirmDto.RegisterCommentReq req = new ConfirmDto.RegisterCommentReq();
        req.setConfirmId(confirmId);
        req.setParentCommentId(commentId);
        req.setContent("ㅇㅈ");

        ConfirmDto.RegisterCommentRes res = confirmService.registerComment(req);
        nestedCommentId = res.getComment().getId();

    }

    private long nestedCommentId = 1L;

    @Test
    void t15_likeComment() {

        ConfirmDto.LikeCommentReq req = new ConfirmDto.LikeCommentReq();
        req.setCommentId(nestedCommentId);
        confirmService.likeComment(req);

        ConfirmDto.CommentListReq commentListReq = getCommentListReq();
        ListRes<ConfirmDto.CommentData> comments = confirmService.comments(commentListReq);
        ConfirmDto.CommentData comment = comments.getDatas().get(0);

        assertThat(comment.getLike()).isZero();
        assertThat(comment.getNested()).isEqualTo(1);

        ConfirmDto.CommentListReq nestedReq = new ConfirmDto.CommentListReq();
        nestedReq.setTargetId(commentId);
        nestedReq.setPage(new PageReq());

        ListRes<ConfirmDto.NestedCommentData> nestedComments = confirmService.nestedComments(nestedReq);
        ConfirmDto.NestedCommentData nested = nestedComments.getDatas().get(0);

        assertThat(nested.getLike()).isEqualTo(1);

    }

    @Test
    void t16_confirms() {
        t02_confirms();
    }

    @Test
    void t17_comments() {
        t06_comments();
    }

}