package com.ootd.be.api.feed;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.ootd.be.AbstractTestBase;
import com.ootd.be.entity.Feed;
import com.ootd.be.entity.FeedRepository;
import com.ootd.be.entity.Member;
import com.ootd.be.entity.MemberRepository;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@SpringBootTest
public class FeedServiceTest extends AbstractTestBase {

    @Resource
    private FeedService feedService;
    @Resource
    private MemberRepository memberRepository;
    @Resource
    private FeedRepository feedRepository;

    @Test
    public void like() {

        Member member = new Member();
        member.setId(1L);
        member.setEmail("aa@aa.com");
        member.setName("test");
        memberRepository.save(member);

        Feed feed = new Feed();
        feed.setId(1L);
        feed.setContents("aaa");
        feedRepository.save(feed);

        feedService.like(1L);
    }

    @Test
    public void saveImage() {

        try {
            BufferedImage image = ImageIO.read(new URL("https://image.msscdn.net/images/goods_img/20200729/1530516/1530516_16775556295425_320.jpg"));

            File dir = new File("~/DATA/temp");
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
                System.out.println("mkdirs = " + mkdirs);
            }
            File file = new File(dir, "temp.jpg");
            System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());
            ImageIO.write(image, "jpg", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}