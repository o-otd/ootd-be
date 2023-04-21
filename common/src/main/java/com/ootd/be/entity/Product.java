package com.ootd.be.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
public class Product extends BaseEntity {

    @Id @Column(name = "product_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private MallCode mallCode;

    private String goodsNo;
    private String goodsName;

    private String brandName;

    private String imagePath;

    private boolean deleted;

    @ToString.Exclude
    @OneToMany
    private List<ProductLike> productLikes;

}
