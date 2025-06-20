package com.zlagoda.Zlagoda.repository;

import com.zlagoda.Zlagoda.dto.stats.EmployeeCategorySalesDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface StatisticsRepository {

    List<EmployeeCategorySalesDTO> productsSoldByEmployeeByCategory(String category_number);
}