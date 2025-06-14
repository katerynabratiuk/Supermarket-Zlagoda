package com.zlagoda.Zlagoda.controller;

import com.zlagoda.Zlagoda.entity.Category;
import com.zlagoda.Zlagoda.entity.StoreProduct;
import com.zlagoda.Zlagoda.service.StoreProductService;
import com.zlagoda.Zlagoda.service.implementation.StoreProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
public class ProductController {

    private final StoreProductServiceImpl storeProductService;

    public ProductController(StoreProductServiceImpl storeProductService) {
        this.storeProductService = storeProductService;
    }

    @GetMapping()
    public @ResponseBody List<StoreProduct> getAllProducts()
    {
        return storeProductService.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody StoreProduct getProductById(@PathVariable String id)
    {
        return storeProductService.findById(id);
    }


    @GetMapping("/promotional")
    public @ResponseBody List<StoreProduct> getPromotionalProducts()
    {
        return storeProductService.findPromotional();
    }

    @GetMapping("/by-category/{category}")
    public @ResponseBody List<StoreProduct> getProductsByCategory(@PathVariable String category)
    {
        return storeProductService.findByCategory(category);
    }

    @PostMapping()
    public void createProduct(@RequestBody @Valid StoreProduct storeProduct)
    {
        storeProductService.create(storeProduct);
    }
}
