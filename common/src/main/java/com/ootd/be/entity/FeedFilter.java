package com.ootd.be.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class FeedFilter extends BaseEntity {

    @Id @Column(name = "feed_filter_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    private FeedFilterType filterType;

    private String value;

    private int min;

    private int max;

}
