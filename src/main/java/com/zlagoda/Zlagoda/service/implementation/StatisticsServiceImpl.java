package com.zlagoda.Zlagoda.service.implementation;

import com.zlagoda.Zlagoda.dto.stats.CitySalesDTO;
import com.zlagoda.Zlagoda.dto.stats.EmployeeDTO;
import com.zlagoda.Zlagoda.dto.stats.PromoOnlyCustomerDTO;
import com.zlagoda.Zlagoda.entity.CustomerCard;
import com.zlagoda.Zlagoda.entity.Employee;
import com.zlagoda.Zlagoda.repository.implementation.StatisticsRepositoryImpl;
import com.zlagoda.Zlagoda.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsRepositoryImpl statisticsRepository;

    public StatisticsServiceImpl(StatisticsRepositoryImpl statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    public List<EmployeeDTO> productsSoldByEmployeeByCategory(Integer category_number) {
        return statisticsRepository.productsSoldByEmployeeByCategory(category_number);
    }

    @Override
    public List<CitySalesDTO> getProductsSoldInCitiesExceptOne(String city) {
        return statisticsRepository.getProductsSoldInCitiesExceptOne(city);
    }

    @Override
    public List<PromoOnlyCustomerDTO> getPromoCustomers() {
        return statisticsRepository.getPromoCustomers();
    }


    public List<CustomerCard> getLoyalCustomers() {
        return statisticsRepository.getLoyalCustomers();
    }

    public List<Employee> getEmployeesWhoNeverSoldCategory(Integer categoryID) {
        return statisticsRepository.getEmployeesWhoNeverSoldCategory(categoryID);
    }
}
