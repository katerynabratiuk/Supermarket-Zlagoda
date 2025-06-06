package com.zlagoda.Zlagoda.controller;

import com.zlagoda.Zlagoda.entity.Category;
import com.zlagoda.Zlagoda.service.implementation.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    public void createCategory(@RequestBody @Valid Category category) {
        categoryService.create(category);
    }

    @PutMapping("/edit/{id}")
    public void editCategory(@RequestBody @Valid Category category) { categoryService.update(category); }

    @DeleteMapping("/{number}")
    public void deleteCategory(@PathVariable Integer number) {
        categoryService.delete(number);
    }

}
