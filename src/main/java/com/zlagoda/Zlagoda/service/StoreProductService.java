package com.zlagoda.Zlagoda.service;

import com.zlagoda.Zlagoda.entity.StoreProduct;

import java.util.List;

public interface StoreProductService{

    List<StoreProduct> findByCategory(String categoryName);
    List<StoreProduct> findByName(String name);
    List<StoreProduct> findAll();
    void delete(String UPC);
    void update(StoreProduct storeProduct);
    StoreProduct findById(String id);
    void create(StoreProduct storeProduct);
}
