package com.zlagoda.Zlagoda.service.implementation;

import com.zlagoda.Zlagoda.dto.stats.EmployeeCategorySalesDTO;
import com.zlagoda.Zlagoda.repository.StatisticsRepository;
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
    public List<EmployeeCategorySalesDTO> productsSoldByEmployeeByCategory(Integer category_number) {
        return statisticsRepository.productsSoldByEmployeeByCategory(category_number);
    }
}
