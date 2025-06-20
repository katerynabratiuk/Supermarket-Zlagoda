package com.zlagoda.Zlagoda.controller;

import com.zlagoda.Zlagoda.entity.Category;
import com.zlagoda.Zlagoda.entity.Employee;
import com.zlagoda.Zlagoda.entity.StoreProduct;
import com.zlagoda.Zlagoda.service.StoreProductService;
import com.zlagoda.Zlagoda.service.implementation.StoreProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
public class ProductController {

    private final StoreProductServiceImpl storeProductService;

    @PutMapping()
    public void updateProduct(@RequestBody @Valid StoreProduct storeProduct)
    {
        storeProductService.update(storeProduct);
    }


    @PostMapping("/promotional")
    public void addPromotionalProduct(@RequestBody @Valid StoreProduct promoProduct) {
        System.out.println(promoProduct);
        storeProductService.addPromotional(promoProduct);
    }


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

    @PostMapping()
    public void createProduct(@RequestBody @Valid StoreProduct storeProduct)
    {
        storeProductService.create(storeProduct);
    }


    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id)
    {
        storeProductService.delete(id);
    }

    @GetMapping("/filter")
    public List<StoreProduct> filterProducts(
            @RequestParam(required = false) Boolean promotional,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String sortBy
    ) {
        List<String> sortParams = new ArrayList<>();
        if (sortBy != null && !sortBy.isBlank()) {
            sortParams.add(sortBy);
        }
        return storeProductService.filter(promotional, category, sortParams);
    }

    @GetMapping("/search")
    public List<StoreProduct> searchProducts(@RequestParam("query") String query) {
        return storeProductService.searchProd(query);
    }
}
