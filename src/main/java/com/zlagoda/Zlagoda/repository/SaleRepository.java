package com.zlagoda.Zlagoda.repository;

import com.zlagoda.Zlagoda.entity.Sale;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SaleRepository{

    List<Sale> findByCheckId(String checkId);
    void create(Sale sale);
}
