package com.ootd.be.api.feed;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.ootd.be.api.feed.FeedDto.ItemReq;
import com.ootd.be.config.security.SecurityHolder;
import com.ootd.be.entity.Feed;
import com.ootd.be.entity.FeedBookmark;
import com.ootd.be.entity.FeedBookmarkRepository;
import com.ootd.be.entity.FeedItemType;
import com.ootd.be.entity.FeedLike;
import com.ootd.be.entity.FeedLikeRepository;
import com.ootd.be.entity.FeedRepository;
import com.ootd.be.entity.Feeditem;
import com.ootd.be.entity.MallCode;
import com.ootd.be.entity.Member;
import com.ootd.be.entity.MemberRepository;
import com.ootd.be.entity.Product;
import com.ootd.be.entity.ProductRepository;
import com.ootd.be.exception.ValidationException;
import com.ootd.be.util.IdGenerator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class FeedService {

    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final FeedBookmarkRepository feedBookmarkRepository;
    private final ProductRepository productRepository;

    public void like(Long feedId) {
        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new ValidationException("피드 정보를 찾을 수 없음"));

        FeedLike feedLike = feedLikeRepository.findByMemberAndFeed(member, feed).orElseGet(FeedLike::new);
        if (feedLike.getId() != null) return;

        feedLike.setId(IdGenerator.I.next());
        feedLike.setMember(member);
        feedLike.setFeed(feed);

        feedLikeRepository.save(feedLike);
    }

    public void dislike(Long feedId) {
        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new ValidationException("피드 정보를 찾을 수 없음"));

        FeedLike feedLike = feedLikeRepository.findByMemberAndFeed(member, feed).orElseGet(FeedLike::new);
        if (feedLike.getId() == null) return;

        feedLikeRepository.delete(feedLike);

    }

    public void addBookmark(Long feedId) {
        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new ValidationException("피드 정보를 찾을 수 없음"));

        FeedBookmark bookmark = feedBookmarkRepository.findByMemberAndFeed(member, feed).orElseGet(FeedBookmark::new);
        if (bookmark.getId() != null) return;

        bookmark.setId(IdGenerator.I.next());
        bookmark.setMember(member);
        bookmark.setFeed(feed);

        feedBookmarkRepository.save(bookmark);
    }

    public void deleteBookmark(Long feedId) {
        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new ValidationException("피드 정보를 찾을 수 없음"));

        FeedBookmark bookmark = feedBookmarkRepository.findByMemberAndFeed(member, feed).orElseGet(FeedBookmark::new);
        if (bookmark.getId() == null) return;

        feedBookmarkRepository.delete(bookmark);
    }

    public void registerFeed(FeedDto.RegisterReq req) {

        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Feed feed = new Feed();
        feed.setId(IdGenerator.I.next());
        feed.setTitle(req.getTitle());
        feed.setContents(req.getContent());
        feed.setMember(member);

        List<Feeditem> outers = saveProducts(feed, FeedItemType.outer, req.getOuters());
        add(feed, outers);
        List<Feeditem> tops = saveProducts(feed, FeedItemType.top, req.getTops());
        add(feed, tops);
        List<Feeditem> bottoms = saveProducts(feed, FeedItemType.bottom, req.getBottoms());
        add(feed, bottoms);
        List<Feeditem> shoes = saveProducts(feed, FeedItemType.shoes, req.getShoes());
        add(feed, shoes);
        List<Feeditem> bags = saveProducts(feed, FeedItemType.bag, req.getBags());
        add(feed, bags);

    }

    public List<Feeditem> saveProducts(Feed feed, FeedItemType type, List<ItemReq> items) {

        if (items.isEmpty()) {return null;}

        List<Feeditem> feeditems = items.stream().map(item ->
                                                            productRepository.findByMallCodeAndGoodsNo(MallCode.musinsa, item.getGoodsNo().toString()).orElseGet(() -> toProduct(item))
                                      )
                                      .map(product -> {
                                          Feeditem feeditem = new Feeditem();
                                          feeditem.setId(IdGenerator.I.next());
                                          feeditem.setFeed(feed);
                                          feeditem.setProduct(product);
                                          feeditem.setType(type);
                                          return feeditem;
                                      }).collect(Collectors.toList());

        add(feed, feeditems);

        return feeditems;
    }

    private void add(Feed feed, List<Feeditem> items) {

        if (items == null) {
            return;
        }

        // fixme.
        if (feed.getItems() == null) {
            feed.setItems(items);
        } else {
            feed.getItems().addAll(items);
        }
    }

    private Product toProduct(ItemReq item) {
        Product product = new Product();
        product.setId(IdGenerator.I.next());
        product.setMallCode(MallCode.musinsa);
        product.setGoodsNo(item.getGoodsNo().toString());
        product.setGoodsName(item.getGoodsName());
        product.setBrandName(item.getBrandName());

        // fixme. 이미지 처리
        product.setImagePath(saveProductImage(item.getImageUrl()));

        return product;
    }

    private String saveProductImage(String imageUrl) {
        String path = "/DATA/attach/";

//        BufferedImage image = ImageIO.read(new URL(imageUrl));
//        File file = new File(path + "temp.png");
//        ImageIO.write(image, "png", file);

        // fixme. something to do...
        return "";
    }





}
