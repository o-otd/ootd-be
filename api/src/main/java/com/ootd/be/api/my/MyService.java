package com.ootd.be.api.my;

import com.ootd.be.config.security.SecurityHolder;
import com.ootd.be.entity.*;
import com.ootd.be.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyService {

    private final FeedRepository feedRepository;

    private final FollowingRepository followingRepository;

    private final MemberRepository memberRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final ProductLikeRepository productLikeRepository;

    private final ConfirmRepository confirmRepository;
    private final ConfirmCommentRepository confirmCommentRepository;

    public MyDto.MyPageResDto likes() {

        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        MyDto.MyPageResDto vo = new MyDto.MyPageResDto();

        Optional<Feed> findFeed = feedRepository.findTopByMemberOrderByCreatedAtDesc(member);
        if (findFeed.isPresent()) {
            vo.setFeed(findFeed.get());
        }

        long followees = followingRepository.countByFollower(member);
        vo.setFolloweeCnt(followees);
        long followers = followingRepository.countByFollowee(member);
        vo.setFollowerCnt(followers);

        long feedLikes = feedLikeRepository.countByMember(member);
        long productLikes = productLikeRepository.countByMember(member);
        // fixme. 컨펌 좋아요 버튼 없음.
        vo.setLikeCnt(feedLikes + productLikes);

        long myConfirms = confirmRepository.countByMember(member);
        vo.setConfirmCnt(myConfirms);

        long confirmComments = confirmCommentRepository.countByMember(member);
        vo.setCommentCnt(confirmComments);

        return vo;

    }

}
