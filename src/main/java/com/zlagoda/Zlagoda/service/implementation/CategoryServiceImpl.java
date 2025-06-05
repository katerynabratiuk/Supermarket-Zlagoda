package com.zlagoda.Zlagoda.service.implementation;

import com.zlagoda.Zlagoda.entity.Category;
import com.zlagoda.Zlagoda.repository.CategoryRepository;
import com.zlagoda.Zlagoda.service.CategoryService;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category findById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void create(Category category) {
        categoryRepository.create(category);
    }

    @Override
    public void update(Category category) {
        categoryRepository.update(category);
    }

    @Override
    public void delete(Integer id) {
        categoryRepository.delete(id);
    }
}
