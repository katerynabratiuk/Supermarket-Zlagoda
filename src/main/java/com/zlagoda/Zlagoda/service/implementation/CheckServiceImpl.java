package com.zlagoda.Zlagoda.service.implementation;

import com.zlagoda.Zlagoda.entity.Receipt;
import com.zlagoda.Zlagoda.entity.Sale;
import com.zlagoda.Zlagoda.entity.StoreProduct;
import com.zlagoda.Zlagoda.exception.NotEnoughProductException;
import com.zlagoda.Zlagoda.repository.CheckRepository;
import com.zlagoda.Zlagoda.repository.SaleRepository;
import com.zlagoda.Zlagoda.repository.StoreProductRepository;
import com.zlagoda.Zlagoda.repository.implementation.CheckRepositoryImpl;
import com.zlagoda.Zlagoda.repository.implementation.SaleRepositoryImpl;
import com.zlagoda.Zlagoda.service.CheckService;
import com.zlagoda.Zlagoda.util.IdGenerator;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CheckServiceImpl implements CheckService {

    private final CheckRepositoryImpl checkRepository;
    private final SaleRepositoryImpl saleRepository;
    private final StoreProductRepository productRepository;
    private final IdGenerator idGenerator;

    public CheckServiceImpl(CheckRepositoryImpl checkRepository,
                            SaleRepositoryImpl saleRepository,
                            StoreProductRepository productRepository,
                            IdGenerator idGenerator) {
        this.checkRepository = checkRepository;
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.idGenerator = idGenerator;
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
        if (check != null)
        {

            ArrayList<Sale> sales = (ArrayList<Sale>) saleRepository.findByCheckId(id);
            check.setSales(sales);
        }
        return check;
    }

    @Override
    public void create(Receipt receipt) {
        if (receipt.getCheckNumber() == null) {
            receipt.setCheckNumber(idGenerator.generate(IdGenerator.Option.Check));
        }

        for (Sale sale : receipt.getProducts()) {
            StoreProduct storeProduct = productRepository.findById(sale.getStoreProduct().getUPC());
            if (storeProduct.getProductsNumber() < sale.getProductNum()) {
                throw new NotEnoughProductException("Not enough stock for product " +
                        storeProduct.getProduct().getName()
                        + "(" + storeProduct.getUPC()+")" );
            }
        }

        checkRepository.create(receipt);

        for (Sale sale : receipt.getProducts()) {
            sale.setCheck(receipt);
            this.saleRepository.create(sale);

            StoreProduct storeProduct = productRepository.findById(sale.getStoreProduct().getUPC());
            int newStock = storeProduct.getProductsNumber() - sale.getProductNum();
            storeProduct.setProductsNumber(newStock);
            productRepository.update(storeProduct);
        }
    }



    @Override
    public void update(Receipt receipt) {
        checkRepository.update(receipt);
    }

    @Override
    public void delete(String id) {
        checkRepository.delete(id);
    }

    public List<Receipt> filter(String cashierId, String sortBy, LocalDate from, LocalDate to) {
        return checkRepository.filter(cashierId, sortBy, from, to);
    }
}
