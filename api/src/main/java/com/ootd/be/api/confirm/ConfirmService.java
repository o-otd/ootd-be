package com.ootd.be.api.confirm;

import com.ootd.be.config.security.SecurityHolder;
import com.ootd.be.entity.*;
import com.ootd.be.exception.ValidationException;
import com.ootd.be.util.IdGenerator;
import com.ootd.be.util.file.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfirmService {

    private final MemberRepository memberRepository;
    private final ConfirmRepository confirmRepository;
    private final ConfirmVoteRepository voteRepository;
    private final ConfirmCommentRepository commentRepository;

    public void registerConfirm(ConfirmDto.RegisterReq req) {

        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Confirm confirm = new Confirm();
        confirm.setId(IdGenerator.I.next());
        confirm.setMember(member);
        confirm.setCompleted(false);
        confirm.setContent(req.getContent());
        confirm.setStartDate(req.getStartDate());
        confirm.setEndDate(req.getEndDate());

        File attachDir = FileManager.I.today("attach", "confirm");

        List<ConfirmImage> confirmImages = req.getImages().stream().map(uploaded -> {

            String extension = FileManager.I.findFileExtension(FileManager.PathType.file, uploaded.getOriginalFilename());
            File imageFile = new File(attachDir, confirm.getId().toString() + "." + extension);

            ConfirmImage confirmImage = new ConfirmImage();
            confirmImage.setId(IdGenerator.I.next());
            confirmImage.setMember(member);
            confirmImage.setConfirm(confirm);
            confirmImage.setImagePath(FileManager.I.relativePath(imageFile));

            try {
                BufferedImage image = ImageIO.read(uploaded.getInputStream());
                ImageIO.write(image, extension, imageFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return confirmImage;
        }).collect(Collectors.toList());

        confirm.setImages(confirmImages);

        confirmRepository.save(confirm);

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

    public void registerComment(ConfirmDto.RegisterCommentReq req) {

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
        comment.setDeleted(true);

    }



}
