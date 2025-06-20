package com.zlagoda.Zlagoda.controller;

import com.zlagoda.Zlagoda.entity.Category;
import com.zlagoda.Zlagoda.entity.StoreProduct;
import com.zlagoda.Zlagoda.service.implementation.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public @ResponseBody List<Category> getCategories()
    {
        return categoryService.findAll();
    }

    @PostMapping()
    public void createCategory(@RequestBody @Valid Category category) {
        categoryService.create(category);
    }

    @PutMapping()
    public void editCategory(@RequestBody @Valid Category category) { categoryService.update(category); }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.delete(id);
    }


    @GetMapping("/filter")
    public List<Category> filterProducts(
            @RequestParam(required = false) String sortBy
    ) {
        return categoryService.filter(sortBy);
    }

}
