package com.zlagoda.Zlagoda.controller;

import com.zlagoda.Zlagoda.entity.Category;
import com.zlagoda.Zlagoda.service.implementation.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(produces="application/json")
    public @ResponseBody List<Category> getCategories()
    {
        return categoryService.findAll();
    }

    @PostMapping
    public void createCategory(@RequestBody @Valid Category category) {
        categoryService.create(category);
    }

    @DeleteMapping("/{number}")
    public void deleteCategory(@PathVariable Integer number) {
        categoryService.delete(number);
    }

}
