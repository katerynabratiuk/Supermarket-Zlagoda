package com.zlagoda.Zlagoda.service;

import com.zlagoda.Zlagoda.entity.Receipt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CheckService{

    List<Receipt> findByEmployeeIdAndDateBetween(String employeeId, LocalDate from, LocalDate to);
    List<Receipt> findByDateBetween(LocalDate from, LocalDate to);
    BigDecimal findSumByEmployeeIdAndDateBetween(String employeeId, LocalDate from, LocalDate to);
    BigDecimal findTotalSalesSumByDateBetween(LocalDate from, LocalDate to);
    int findTotalQuantitySoldByProductIdAndDateBetween(String productId, LocalDate startDate, LocalDate endDate);
    List<Receipt> findByEmployeeId(String employeeId);
    List<Receipt> findAll();
    List<Receipt> findByName(String name);
    Receipt findById(String id);
    void create(Receipt receipt);
    void update(Receipt receipt);
    void delete(String id);
}
