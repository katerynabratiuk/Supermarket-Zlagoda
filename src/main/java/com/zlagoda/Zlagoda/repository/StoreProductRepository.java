package com.zlagoda.Zlagoda.repository;

import com.zlagoda.Zlagoda.entity.StoreProduct;
import jakarta.validation.Valid;

import java.util.List;

public interface StoreProductRepository extends GenericRepository<StoreProduct, String>{

    List<StoreProduct> findByCategory(String categoryName);
    List<StoreProduct> findByName(String name);
    List<StoreProduct> filter(Boolean promotional, String category, List<String> sortBy);

    void addPromotional(@Valid StoreProduct promoProduct);
}
