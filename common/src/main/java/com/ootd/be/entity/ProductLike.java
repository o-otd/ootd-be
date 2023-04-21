package com.ootd.be.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ProductLike extends BaseEntity {

    @Id @Column(name = "product_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

}
