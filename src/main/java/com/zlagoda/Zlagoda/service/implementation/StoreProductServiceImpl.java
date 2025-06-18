package com.zlagoda.Zlagoda.service.implementation;

import com.zlagoda.Zlagoda.entity.Product;
import com.zlagoda.Zlagoda.entity.StoreProduct;
import com.zlagoda.Zlagoda.repository.ProductRepository;
import com.zlagoda.Zlagoda.repository.StoreProductRepository;
import com.zlagoda.Zlagoda.service.StoreProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreProductServiceImpl implements StoreProductService {

    private final StoreProductRepository storeProductRepository;
    private final ProductRepository productRepository;

    public StoreProductServiceImpl(StoreProductRepository storeProductRepository, ProductRepository productRepository) {
        this.storeProductRepository = storeProductRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<StoreProduct> findByCategory(String categoryName) {
        return storeProductRepository.findByCategory(categoryName);
    }

    @Override
    public List<StoreProduct> findAll() {
        return storeProductRepository.findAll();
    }

    @Override
    public List<StoreProduct> findByName(String name) {
        return storeProductRepository.findByName(name);
    }

    @Override
    public StoreProduct findById(String id) {
        return storeProductRepository.findById(id);
    }

    @Override
    public void create(StoreProduct storeProduct)
    {
        productRepository.create(storeProduct.getProduct());
        List<Product> products = productRepository.findByName(storeProduct.getProduct().getName());
        storeProduct.setProduct(products.getFirst());
        storeProductRepository.create(storeProduct);

    }

    @Override
    public void update(StoreProduct storeProduct) {
        storeProductRepository.update(storeProduct);
    }

    @Override
    public void delete(String id) {
        storeProductRepository.delete(id);
    }

    public List<StoreProduct> filter(Boolean promotional, String category, List<String> sortBy)
    {
        return storeProductRepository.filter(promotional, category, sortBy);

    }
}
