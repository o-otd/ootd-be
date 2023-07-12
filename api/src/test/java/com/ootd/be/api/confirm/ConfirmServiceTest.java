package com.ootd.be.api.confirm;

import com.ootd.be.api.ListReq;
import com.ootd.be.api.ListRes;
import com.ootd.be.api.PageReq;
import com.ootd.be.api.ServiceTestBase;
import com.ootd.be.entity.ConfirmVote;
import com.ootd.be.entity.ConfirmVoteType;
import com.ootd.be.util.GridPrinter;
import com.ootd.be.util.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        req.setContent("이거 어때요?");
        req.setStartDate("20230501");
        req.setEndDate("20230531");

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
        req1.setWording("1111");

        ConfirmDto.VoteTypeReq req2 = new ConfirmDto.VoteTypeReq();
        req2.setOrder(2);
        req2.setWording("2222");

        req.setVoteTypeReqs(List.of(req1, req2));

        ConfirmDto.ConfirmData res = confirmService.registerConfirm(req);
        confirmId = res.getId();
        confirmVoteTypeId = res.getVotes().get(0).getVoteTypeId();

    }

    @Test
    public void t02_confirms() {

        PageReq page = new PageReq();
        page.setPage(0);
        page.setSize(10);

        ListReq req = new ListReq();
        req.setPage(page);

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

    @Test
    void t03_vote() {

        ConfirmDto.VoteReq req = new ConfirmDto.VoteReq();
        req.setConfirmId(confirmId);
        req.setVoteTypeId(confirmVoteTypeId);
        confirmService.vote(req);

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

    }

    @Test
    void t06_comments() {

        ConfirmDto.CommentListReq req = new ConfirmDto.CommentListReq();
        req.setTargetId(confirmId);

        PageReq page = new PageReq();
        page.setPage(0);
        page.setSize(10);

        req.setPage(page);

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

    }

    @Test
    void t10_comments() {
        t06_comments();
    }

    @Test
    void t11_modifyComment() {

        ConfirmDto.ModifyCommentReq req = new ConfirmDto.ModifyCommentReq();
        req.setCommentId(commentId);
        req.setContent("so so");
        confirmService.modifyComment(req);

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

    }

    @Test
    void t14_registerNestedComment() {

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