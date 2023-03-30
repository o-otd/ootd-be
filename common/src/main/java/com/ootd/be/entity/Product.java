package com.ootd.be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

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

}
