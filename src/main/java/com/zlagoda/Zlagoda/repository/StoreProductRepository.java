package com.zlagoda.Zlagoda.repository;

import com.zlagoda.Zlagoda.entity.StoreProduct;

import java.util.List;

public interface StoreProductRepository extends GenericRepository<StoreProduct, String>{

    List<StoreProduct> findByCategory(String categoryName);
    List<StoreProduct> findByName(String name);
    List<StoreProduct> findPromotional();
    List<StoreProduct> findNonPromotional();
}
