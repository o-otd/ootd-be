package com.ootd.be.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByMallCodeAndGoodsNo(MallCode mallCode, String goodsNo);
    List<Product> findByMallCodeAndGoodsNoIsIn(MallCode mallCode, List<String> goodsNos);

}
