package com.zlagoda.Zlagoda.repository;

import com.zlagoda.Zlagoda.entity.CustomerCard;

import java.util.List;

public interface CustomerCardRepository extends GenericRepository<CustomerCard, String>{

    List<CustomerCard> filter(Integer percentage, List<String> sortParams);
}
