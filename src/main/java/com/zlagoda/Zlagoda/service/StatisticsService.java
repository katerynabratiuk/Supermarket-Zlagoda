package com.zlagoda.Zlagoda.service;

import com.zlagoda.Zlagoda.dto.stats.CitySalesDTO;
import com.zlagoda.Zlagoda.dto.stats.EmployeeCategorySalesDTO;

import java.util.List;

public interface StatisticsService {

    List<EmployeeCategorySalesDTO> productsSoldByEmployeeByCategory(Integer category_number);
    List<CitySalesDTO> getProductsSoldInCitiesExceptOne(String city);
}
