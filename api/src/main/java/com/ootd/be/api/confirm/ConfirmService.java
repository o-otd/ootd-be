package com.ootd.be.api.confirm;

import com.ootd.be.config.security.SecurityHolder;
import com.ootd.be.entity.*;
import com.ootd.be.exception.ValidationException;
import com.ootd.be.util.IdGenerator;
import com.ootd.be.util.file.FileManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ConfirmService {

    private final MemberRepository memberRepository;
    private final ConfirmRepository confirmRepository;
    private final ConfirmVoteRepository voteRepository;
    private final ConfirmCommentRepository commentRepository;
    private final ConfirmCommentLikeRepository commentLikeRepository;

    public ConfirmDto.RegisterRes registerConfirm(ConfirmDto.RegisterReq req) {

        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Confirm confirm = new Confirm();
        confirm.setId(IdGenerator.I.next());
        confirm.setMember(member);
        confirm.setCompleted(false);
        confirm.setContent(req.getContent());
        confirm.setStartDate(req.getStartDate());
        confirm.setEndDate(req.getEndDate());
        confirm.setNew(true);

        File attachDir = FileManager.I.today("attach", "confirm", confirm.getId().toString());

        List<ConfirmImage> confirmImages = req.getImages().stream().map(uploaded -> {

            ConfirmImage confirmImage = new ConfirmImage();
            confirmImage.setId(IdGenerator.I.next());
            confirmImage.setMember(member);
            confirmImage.setConfirm(confirm);

            String extension = FileManager.I.findFileExtension(FileManager.PathType.file, uploaded.getOriginalFilename());
            File imageFile = new File(attachDir, confirmImage.getId() + "." + extension);
            log.debug("image : {} -> {}", uploaded.getOriginalFilename(), imageFile.getAbsolutePath());

            confirmImage.setImagePath(FileManager.I.relativePath(imageFile));
            confirmImage.setNew(true);

            try {
                BufferedImage image = ImageIO.read(uploaded.getInputStream());
                ImageIO.write(image, extension, imageFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return confirmImage;
        }).collect(Collectors.toList());

//        List<ConfirmImage> savedImages = confirmImageRepository.saveAll(confirmImages);

        confirm.setImages(confirmImages);

        confirmRepository.save(confirm);

        return ConfirmDto.RegisterRes.of(confirm.getId());

    }

    public void vote(ConfirmDto.VoteReq req) {
        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Confirm confirm = confirmRepository.findById(req.getConfirmId()).orElseThrow(() -> new ValidationException("유효하지 않은 컨펌"));

        Optional<ConfirmVote> findVote = voteRepository.findByVoterAndConfirm(member, confirm);
        if (req.getVoteType() == ConfirmVoteType.cancel) {
            findVote.ifPresent(vote -> voteRepository.delete(vote));
        } else {
            ConfirmVote vote = findVote.orElseGet(() -> {
                ConfirmVote newVote = new ConfirmVote();
                newVote.setId(IdGenerator.I.next());
                newVote.setVoter(member);
                newVote.setConfirm(confirm);
                return newVote;
            });
            vote.setVoteType(req.getVoteType());
            voteRepository.save(vote);
        }
    }

    public ConfirmDto.RegisterCommentRes registerComment(ConfirmDto.RegisterCommentReq req) {

        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Confirm confirm = confirmRepository.findById(req.getConfirmId()).orElseThrow(() -> new ValidationException("유효하지 않은 컨펌"));

        ConfirmComment comment = new ConfirmComment();
        comment.setId(IdGenerator.I.next());
        comment.setContent(req.getContent());
        comment.setMember(member);
        comment.setConfirm(confirm);
        if (req.getParentCommentId() != null) {
            ConfirmComment parentComment = commentRepository.findById(req.getParentCommentId()).orElseThrow(() -> new ValidationException("유효하지 않은 상위 댓글"));
            comment.addTo(parentComment);
        } else {
            comment.setRootComment(comment);
        }

        commentRepository.save(comment);

        return ConfirmDto.RegisterCommentRes.of(comment.getId());

    }

    public void modifyComment(ConfirmDto.ModifyCommentReq req) {

        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        ConfirmComment comment = commentRepository.findById(req.getCommentId()).orElseThrow(() -> new ValidationException("유효하지 않은 댓글"));
        comment.setContent(req.getContent());

    }

    public void deleteComment(ConfirmDto.DeleteCommentReq req) {

        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        ConfirmComment comment = commentRepository.findById(req.getCommentId()).orElseThrow(() -> new ValidationException("유효하지 않은 댓글"));

        if (!comment.getMember().equals(member)) {
            throw new ValidationException("본인이 작성한 댓글만 삭제 가능");
        }

        comment.setDeleted(true);

    }

    public ConfirmDto.ListRes<ConfirmDto.ConfirmData> confirms(ConfirmDto.ListReq req) {

        Member auth = SecurityHolder.get();
        Optional<Member> findMember = memberRepository.findByEmail(auth.getEmail());

        Pageable pageable = req.getPage().toPageRequest(Sort.by(Sort.Order.desc("createdAt")));

        Page<Confirm> all = confirmRepository.findAll(pageable);

        List<ConfirmDto.ConfirmData> datas = all.stream().map(confirm -> {
            ConfirmDto.ConfirmData data = ConfirmDto.ConfirmData.from(confirm);

            ConfirmComment bestComment = commentRepository.best(confirm);
            data.setBestComment(ConfirmDto.CommentData.from(bestComment));

            findMember.ifPresent(member -> {
                Optional<ConfirmVote> findVote = voteRepository.findByVoterAndConfirm(member, confirm);
                findVote.ifPresent(vote -> {
                    data.setMyVoting(vote.getVoteType());
                });
            });

            return data;
        }).collect(Collectors.toList());

        ConfirmDto.PageRes pageRes = ConfirmDto.PageRes.of(all);
        return ConfirmDto.ListRes.of(pageRes, datas);

    }

    public ConfirmDto.ListRes<ConfirmDto.CommentData> comments(ConfirmDto.CommentListReq req) {

        Member auth = SecurityHolder.get();
        Optional<Member> findMember = memberRepository.findByEmail(auth.getEmail());

        Confirm confirm = confirmRepository.findById(req.getTargetId()).orElseThrow(() -> new ValidationException("유효하지 않은 글"));

        Pageable pageable = req.getPage().toPageRequest();
        Page<ConfirmComment> comments = commentRepository.findAllByConfirm(confirm, pageable);
        List<ConfirmDto.CommentData> res = comments.stream().map(comment -> {
            ConfirmDto.CommentData data = ConfirmDto.CommentData.from(comment);
            findMember.ifPresent(member -> {
                if (comment.getMember().equals(member)) {
                    data.setMyComment(true);
                }

                // left join . count로 빼도 될 것 같은데.. 귀찮으니까 그냥 씀.
                if (comment.getLikes().stream().filter(like -> like.getMember().equals(member)).count() > 0L) {
                    data.setMyLike(true);
                }
            });

            return data;
        }).collect(Collectors.toList());

        ConfirmDto.PageRes pageRes = ConfirmDto.PageRes.of(comments);
        return ConfirmDto.ListRes.of(pageRes, res);

    }

    public ConfirmDto.ListRes<ConfirmDto.NestedCommentData> nestedComments(ConfirmDto.CommentListReq req) {

        Member auth = SecurityHolder.get();
        Optional<Member> findMember = memberRepository.findByEmail(auth.getEmail());

        ConfirmComment parentComment = commentRepository.findById(req.getTargetId()).orElseThrow(() -> new ValidationException("유효하지 않은 댓글"));

        Pageable pageable = req.getPage().toPageRequest();
        Page<ConfirmComment> comments = commentRepository.findAllByComment(parentComment, pageable);
        List<ConfirmDto.NestedCommentData> res = comments.stream().map(comment -> {
            ConfirmDto.NestedCommentData data = ConfirmDto.NestedCommentData.from(comment);
            findMember.ifPresent(member -> {
                if (comment.getMember().equals(member)) {
                    data.setMyComment(true);
                }

                // left join . count로 빼도 될 것 같은데.. 귀찮으니까 그냥 씀.
                if (comment.getLikes().stream().filter(like -> like.getMember().equals(member)).count() > 0L) {
                    data.setMyLike(true);
                }
            });

            return data;
        }).collect(Collectors.toList());

        ConfirmDto.PageRes pageRes = ConfirmDto.PageRes.of(comments);
        return ConfirmDto.ListRes.of(pageRes, res);

    }



    public void likeComment(ConfirmDto.LikeCommentReq req) {

        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        ConfirmComment comment = commentRepository.findById(req.getCommentId()).orElseThrow(() -> new ValidationException("유효하지 않은 댓글"));

        Optional<ConfirmCommentLike> findLike = commentLikeRepository.findByCommentAndMember(comment, member);
        if (findLike.isPresent()) {
            return;
        }

        ConfirmCommentLike like = new ConfirmCommentLike();
        like.setId(IdGenerator.I.next());
        like.setComment(comment);
        like.setMember(member);

        commentLikeRepository.save(like);
    }

    public void dislikeComment(ConfirmDto.LikeCommentReq req) {

        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        ConfirmComment comment = commentRepository.findById(req.getCommentId()).orElseThrow(() -> new ValidationException("유효하지 않은 댓글"));

        Optional<ConfirmCommentLike> findLike = commentLikeRepository.findByCommentAndMember(comment, member);
        if (findLike.isEmpty()) {
            return;
        }

        ConfirmCommentLike like = findLike.get();
        if (!like.getMember().equals(member)) throw new ValidationException("타인의 좋아요");

        commentLikeRepository.delete(like);

    }

}


