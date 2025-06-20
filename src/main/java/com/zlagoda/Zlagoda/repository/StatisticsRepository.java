package com.zlagoda.Zlagoda.repository;

import com.zlagoda.Zlagoda.dto.stats.CitySalesDTO;
import com.zlagoda.Zlagoda.dto.stats.EmployeeCategorySalesDTO;
import com.zlagoda.Zlagoda.dto.stats.PromoOnlyCustomerDTO;

import java.util.List;

public interface StatisticsRepository {

    List<EmployeeCategorySalesDTO> productsSoldByEmployeeByCategory(Integer category_number);
    List<CitySalesDTO> getProductsSoldInCitiesExceptOne(String city);
    List<PromoOnlyCustomerDTO> getPromoCustomers();
}