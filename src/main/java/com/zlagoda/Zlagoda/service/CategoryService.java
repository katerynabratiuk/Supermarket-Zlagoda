package com.zlagoda.Zlagoda.service;

import com.zlagoda.Zlagoda.entity.Category;

import java.util.List;

public interface CategoryService{

    List<Category> findAll();
    List<Category> findByName(String name);
    Category findById(Integer id);
    void create(Category category);
    void update(Category category);
    void delete(Integer id);
}
