package com.zlagoda.Zlagoda.repository;

import com.zlagoda.Zlagoda.entity.CustomerCard;

import java.util.List;

public interface CustomerCardRepository extends GenericRepository<CustomerCard, String>{

    // Отримати інформацію про усіх постійних клієнтів, що мають карту клієнта із певним відсотком, посортованих за прізвищем;
    List<CustomerCard> findByPercentage(int percentage);
}
