package com.zlagoda.Zlagoda.service.implementation;

import com.zlagoda.Zlagoda.entity.Receipt;
import com.zlagoda.Zlagoda.entity.Sale;
import com.zlagoda.Zlagoda.repository.CheckRepository;
import com.zlagoda.Zlagoda.repository.SaleRepository;
import com.zlagoda.Zlagoda.service.CheckService;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CheckServiceImpl implements CheckService {

    private final CheckRepository checkRepository;
    private final SaleRepository saleRepository;

    public CheckServiceImpl(CheckRepository checkRepository, SaleRepository saleRepository) {
        this.checkRepository = checkRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public List<Receipt> findByEmployeeIdAndDateBetween(String employeeId, LocalDate from, LocalDate to) {
        return checkRepository.findByEmployeeIdAndDateBetween(employeeId, from, to);
    }

    @Override
    public List<Receipt> findByDateBetween(LocalDate from, LocalDate to) {
        return checkRepository.findByDateBetween(from, to);
    }

    @Override
    public BigDecimal findSumByEmployeeIdAndDateBetween(String employeeId, LocalDate from, LocalDate to) {
        return checkRepository.findSumByEmployeeIdAndDateBetween(employeeId, from, to);
    }

    @Override
    public BigDecimal findTotalSalesSumByDateBetween(LocalDate from, LocalDate to) {
        return checkRepository.findTotalSalesSumByDateBetween(from, to);
    }

    @Override
    public int findTotalQuantitySoldByProductIdAndDateBetween(String productId, LocalDate startDate, LocalDate endDate) {
        return checkRepository.findTotalQuantitySoldByProductIdAndDateBetween(productId, startDate, endDate);
    }

    @Override
    public List<Receipt> findByEmployeeId(String employeeId) {
        return checkRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<Receipt> findAll() {
        return checkRepository.findAll();
    }

    @Override
    public List<Receipt> findByName(String name) {
        // If receipt doesn't have a "name" field, throw or return empty
        throw new UnsupportedOperationException("Receipt has no name field");
    }

    @Override
    public Receipt findById(String id) {
        Receipt check = checkRepository.findById(id);
        ArrayList<Sale> sales = (ArrayList<Sale>) saleRepository.findByCheckId(id);
        check.setSales(sales);
        return check;
    }

    @Override
    public void create(Receipt receipt) {
        checkRepository.create(receipt);
    }

    @Override
    public void update(Receipt receipt) {
        checkRepository.update(receipt);
    }

    @Override
    public void delete(String id) {
        checkRepository.delete(id);
    }
}
