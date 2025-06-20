package com.zlagoda.Zlagoda.repository;

import com.zlagoda.Zlagoda.dto.stats.ProductsSoldByEmployeeDTO;
import org.springframework.web.bind.annotation.PathVariable;

public interface StatisticsRepository {

    ProductsSoldByEmployeeDTO productsSoldByEmployeeByCategory(String category_number);
}
