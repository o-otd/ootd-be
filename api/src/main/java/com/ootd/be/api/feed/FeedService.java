package com.ootd.be.api.feed;

import com.ootd.be.api.feed.FeedDto.ItemReq;
import com.ootd.be.config.security.SecurityHolder;
import com.ootd.be.entity.*;
import com.ootd.be.exception.ValidationException;
import com.ootd.be.util.IdGenerator;
import com.ootd.be.util.file.FileManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class FeedService {

    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final FeedBookmarkRepository feedBookmarkRepository;
    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;
    private final FollowingRepository followingRepository;


    public void follow(Long feedId) {
        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new ValidationException("피드 정보를 찾을 수 없음"));

        Following following = new Following();
        following.setId(IdGenerator.I.next());
        following.setFollower(member);
        following.setFollowee(feed.getMember());

        followingRepository.save(following);
    }

    public void unFollow(Long feedId) {
        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new ValidationException("피드 정보를 찾을 수 없음"));

        followingRepository.findByFollowerAndFollowee(member, feed.getMember()).ifPresent(following -> followingRepository.delete(following));
    }

    public void like(Long feedId) {
        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new ValidationException("피드 정보를 찾을 수 없음"));

        feedLikeRepository.findByMemberAndFeed(member, feed).orElseGet(() -> {
            FeedLike feedLike = new FeedLike();
            feedLike.setId(IdGenerator.I.next());
            feedLike.setMember(member);
            feedLike.setFeed(feed);
            return feedLikeRepository.save(feedLike);
        });

    }

    public void dislike(Long feedId) {
        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new ValidationException("피드 정보를 찾을 수 없음"));

        feedLikeRepository.findByMemberAndFeed(member, feed).ifPresent(feedLikeRepository::delete);

    }

    public void addBookmark(Long feedId) {
        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new ValidationException("피드 정보를 찾을 수 없음"));

        feedBookmarkRepository.findByMemberAndFeed(member, feed).orElseGet(() -> {
            FeedBookmark bookmark = new FeedBookmark();
            bookmark.setId(IdGenerator.I.next());
            bookmark.setMember(member);
            bookmark.setFeed(feed);
            feedBookmarkRepository.save(bookmark);
            return bookmark;
        });

    }

    public void deleteBookmark(Long feedId) {
        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new ValidationException("피드 정보를 찾을 수 없음"));

        feedBookmarkRepository.findByMemberAndFeed(member, feed).ifPresent(feedBookmarkRepository::delete);
    }

    public void registerFeed(FeedDto.RegisterReq req) {

        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Feed feed = new Feed();
        feed.setId(IdGenerator.I.next());
        feed.setTitle(req.getTitle());
        feed.setContents(req.getContent());
        feed.setMember(member);

        File attachDir = FileManager.I.today("attach", "feed", "main");

        String extension = FileManager.I.findFileExtension(FileManager.PathType.file, req.getMainImage().getOriginalFilename());
        File imageFile = new File(attachDir, feed.getId().toString() + "." + extension);

        feed.setMainImage(FileManager.I.relativePath(imageFile));

        try {
            BufferedImage image = ImageIO.read(req.getMainImage().getInputStream());
            ImageIO.write(image, extension, imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<FeedHashTag> hashTags = req.getHashtags().stream().map(tag -> {
            FeedHashTag hashTag = new FeedHashTag();
            hashTag.setId(IdGenerator.I.next());
            hashTag.setFeed(feed);
            hashTag.setHashTag(tag);
            return hashTag;
        }).collect(Collectors.toList());
        feed.setFeedHashTags(hashTags);

        List<FeedFilter> filters = toEntity(feed, req.getFilter());
        feed.setFeedFilters(filters);

        saveProducts(feed, req);

        feedRepository.save(feed);

    }

    public void productLike(Long feedId, Long productId) {

        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Product product = productRepository.findById(productId).orElseThrow(() -> new ValidationException("상품 정보를 찾을 수 없음"));

        Optional<ProductLike> findProductLike = productLikeRepository.findByMemberAndProduct(member, product);
        if (findProductLike.isPresent()) {
            return;
        }

        ProductLike like = new ProductLike();
        like.setId(IdGenerator.I.next());
        like.setMember(member);
        like.setProduct(product);

        productLikeRepository.save(like);

    }

    public void productDislike(Long feedId, Long productId) {

        Member auth = SecurityHolder.get();
        Member member = memberRepository.findByEmail(auth.getEmail()).orElseThrow(() -> new ValidationException("회원 정보를 찾을 수 없음"));

        Product product = productRepository.findById(productId).orElseThrow(() -> new ValidationException("상품 정보를 찾을 수 없음"));

        productLikeRepository.findByMemberAndProduct(member, product).ifPresent(like -> {
            productLikeRepository.delete(like);
        });

    }

    private List<FeedFilter> toEntity(Feed feed, FeedDto.FilterReq filterReq) {

        List<FeedFilter> filters = new ArrayList<>();

        FeedFilter genderFilter = new FeedFilter();
        genderFilter.setId(IdGenerator.I.next());
        genderFilter.setFeed(feed);
        genderFilter.setFilterType(FeedFilterType.gender);
        genderFilter.setValue(filterReq.getGender().name());
        filters.add(genderFilter);

        FeedFilter styleFilter = new FeedFilter();
        styleFilter.setId(IdGenerator.I.next());
        styleFilter.setFeed(feed);
        styleFilter.setFilterType(FeedFilterType.style);
        styleFilter.setValue(filterReq.getStyle().name());
        filters.add(styleFilter);

        FeedFilter ageFilter = new FeedFilter();
        ageFilter.setId(IdGenerator.I.next());
        ageFilter.setFeed(feed);
        ageFilter.setFilterType(FeedFilterType.age);
        ageFilter.setValue(String.valueOf(filterReq.getAge()));
        filters.add(ageFilter);

        FeedFilter heightFilter = new FeedFilter();
        heightFilter.setId(IdGenerator.I.next());
        heightFilter.setFeed(feed);
        heightFilter.setFilterType(FeedFilterType.height);
        heightFilter.setMin(filterReq.getHeight().getMin());
        heightFilter.setMax(filterReq.getHeight().getMax());
        filters.add(heightFilter);

        FeedFilter weightFilter = new FeedFilter();
        weightFilter.setId(IdGenerator.I.next());
        weightFilter.setFeed(feed);
        weightFilter.setFilterType(FeedFilterType.weight);
        weightFilter.setMin(filterReq.getHeight().getMin());
        weightFilter.setMax(filterReq.getHeight().getMax());
        filters.add(weightFilter);

        return filters;
    }

    private void saveProducts(Feed feed, FeedDto.RegisterReq req) {
        saveProducts(feed, FeedItemType.outer, req.getOuters());
        saveProducts(feed, FeedItemType.top, req.getTops());
        saveProducts(feed, FeedItemType.bottom, req.getBottoms());
        saveProducts(feed, FeedItemType.shoes, req.getShoes());
        saveProducts(feed, FeedItemType.bag, req.getBags());
    }

    private void saveProducts(Feed feed, FeedItemType type, List<ItemReq> items) {
        if (items.isEmpty()) {return;}

        List<FeedItem> feedItems = items.stream().map(item ->
                                                            productRepository.findByMallCodeAndGoodsNo(MallCode.musinsa, item.getGoodsNo().toString())
                                                                    .orElseGet(() -> toProduct(item))
                                      )
                                      .map(product -> {
                                          FeedItem feeditem = new FeedItem();
                                          feeditem.setId(IdGenerator.I.next());
                                          feeditem.setFeed(feed);
                                          feeditem.setProduct(product);
                                          feeditem.setType(type);
                                          return feeditem;
                                      }).collect(Collectors.toList());

        add(feed, feedItems);
    }

    private void add(Feed feed, List<FeedItem> items) {
        if (items == null) {
            return;
        }

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

        product.setImagePath(saveProductImage(item.getImageUrl()));

        return product;
    }

    private String saveProductImage(String imageUrl) {

        File attach = FileManager.I.today("attach", "feed", "product");

        String fileName = FileManager.I.findFileName(FileManager.PathType.url, imageUrl);
        File file = new File(attach,  fileName);

        String extension = FileManager.I.findFileExtension(FileManager.PathType.file, fileName);

        try {
            BufferedImage image = ImageIO.read(new URL(imageUrl));
            ImageIO.write(image, extension, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return FileManager.I.relativePath(file);

    }

}
