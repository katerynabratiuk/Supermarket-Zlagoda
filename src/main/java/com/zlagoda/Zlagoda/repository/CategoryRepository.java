package com.zlagoda.Zlagoda.repository;

import com.zlagoda.Zlagoda.entity.Category;

import java.util.List;

public interface CategoryRepository extends GenericRepository<Category,Integer>{

    List<Category> filter(String sortBy);
}
