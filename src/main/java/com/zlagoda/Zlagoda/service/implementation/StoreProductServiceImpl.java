package com.zlagoda.Zlagoda.service.implementation;

import com.zlagoda.Zlagoda.entity.Product;
import com.zlagoda.Zlagoda.entity.StoreProduct;
import com.zlagoda.Zlagoda.repository.ProductRepository;
import com.zlagoda.Zlagoda.repository.StoreProductRepository;
import com.zlagoda.Zlagoda.service.StoreProductService;
import jakarta.validation.Valid;
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
    public List<StoreProduct> findAll() {
        return storeProductRepository.findAll();
    }

    @Override
    public List<StoreProduct> findByName(String name) {
        List<StoreProduct> all = storeProductRepository.findByName(name);

        List<StoreProduct> promotional = all.stream()
                .filter(StoreProduct::isPromotional)
                .toList();

        if (!promotional.isEmpty()) {
            return promotional;
        }

        return all;
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
        productRepository.update(storeProduct.getProduct());
    }

    @Override
    public void delete(String id) {
        storeProductRepository.delete(id);
    }

    public List<StoreProduct> filter(Boolean promotional, String category, List<String> sortBy)
    {
        return storeProductRepository.filter(promotional, category, sortBy);

    }

    public void addPromotional(@Valid StoreProduct promoProduct) {

        storeProductRepository.addPromotional(promoProduct);

    }

    public void deletePromotional(String id) {

    }

    @Override
    public List<StoreProduct> searchProd(String query) {
        return storeProductRepository.searchProd(query);
    }
}
