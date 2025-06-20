package com.zlagoda.Zlagoda.service;

import com.zlagoda.Zlagoda.dto.stats.EmployeeCategorySalesDTO;

import java.util.List;

public interface StatisticsService {

    List<EmployeeCategorySalesDTO> productsSoldByEmployeeByCategory(String category_number);
}
