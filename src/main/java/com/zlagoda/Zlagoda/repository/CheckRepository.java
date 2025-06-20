package com.zlagoda.Zlagoda.repository;

import com.zlagoda.Zlagoda.entity.Receipt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CheckRepository extends GenericRepository<Receipt,String>{

    // Менеджер
    //    Отримати інформацію про всі чеки, створені певним касиром за певний період часу
    //    (з можливістю перегляду куплених товарів у цьому чеку: їх назви, кількості та ціни).
    List<Receipt> findByEmployeeIdAndDateBetween(String employeeId, LocalDate from, LocalDate to);


    //    Отримати інформацію про всі чеки, створені всіма касирами за певний період часу
    //    (з можливістю перегляду куплених товарів у цьому чеку: їх назви, кількості та ціни).
    List<Receipt> findByDateBetween(LocalDate from, LocalDate to);

    //    Визначити загальну суму проданих товарів з чеків, створених певним касиром за певний період часу.
    BigDecimal findSumByEmployeeIdAndDateBetween(String employeeId, LocalDate from, LocalDate to);

    //    Визначити загальну суму проданих товарів з чеків, створених усіма касирами за певний період часу.
    BigDecimal findTotalSalesSumByDateBetween(LocalDate from, LocalDate to);

    //    Визначити загальну кількість одиниць певного товару, проданого за певний період часу.
    int findTotalQuantitySoldByProductIdAndDateBetween(String productId, LocalDate startDate, LocalDate endDate);

    // Касир
    // Переглянути список усіх чеків, що створив касир за певний період часу;
    List<Receipt> findByEmployeeId(String employeeId);

    List<Receipt> filter(String cashierId, String sortBy, LocalDate from, LocalDate to);
}
