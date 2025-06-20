package com.zlagoda.Zlagoda.repository;

import com.zlagoda.Zlagoda.dto.stats.CitySalesDTO;
import com.zlagoda.Zlagoda.dto.stats.EmployeeDTO;
import com.zlagoda.Zlagoda.dto.stats.PromoOnlyCustomerDTO;
import com.zlagoda.Zlagoda.entity.Employee;

import java.util.List;

public interface StatisticsRepository {

    List<EmployeeDTO> productsSoldByEmployeeByCategory(Integer category_number);
    List<CitySalesDTO> getProductsSoldInCitiesExceptOne(String city);
    List<PromoOnlyCustomerDTO> getPromoCustomers();

    List<Employee> getEmployeesWhoNeverSoldCategory(Integer categoryID);
}