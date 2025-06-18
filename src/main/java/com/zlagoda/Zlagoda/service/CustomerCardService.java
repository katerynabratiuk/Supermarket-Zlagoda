package com.zlagoda.Zlagoda.service;

import com.zlagoda.Zlagoda.entity.CustomerCard;

import java.util.List;

public interface CustomerCardService{

    List<CustomerCard> findAll();
    List<CustomerCard> findByName(String name);
    CustomerCard findById(String id);
    void create(CustomerCard card);
    void update(CustomerCard card);
    void delete(String id);
}
