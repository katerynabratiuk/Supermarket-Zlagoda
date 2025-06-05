package com.zlagoda.Zlagoda.service;

import com.zlagoda.Zlagoda.entity.CustomerCard;

import java.util.List;

public interface CustomerCardService{

    // Отримати інформацію про усіх постійних клієнтів, що мають карту клієнта із певним відсотком, посортованих за прізвищем;
    List<CustomerCard> findByPercentage(int percentage);
    List<CustomerCard> findAll();
    List<CustomerCard> findByName(String name);
    CustomerCard findById(String id);
    void create(CustomerCard card);
    void update(CustomerCard card);
    void delete(String id);
}
