package com.zlagoda.Zlagoda.controller;

import com.zlagoda.Zlagoda.dto.stats.CitySalesDTO;
import com.zlagoda.Zlagoda.dto.stats.EmployeeCategorySalesDTO;
import com.zlagoda.Zlagoda.service.implementation.StatisticsServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatisticsController {

    private final StatisticsServiceImpl statisticsService;

    public StatisticsController(StatisticsServiceImpl statisticsService) {
        this.statisticsService = statisticsService;
    }


    @GetMapping("/productsSoldByEmployee/{categoryID}")
    public List<EmployeeCategorySalesDTO> productsSoldByEmployee(@PathVariable Integer categoryID)
    {
        return statisticsService.productsSoldByEmployeeByCategory(categoryID);
    }

    @GetMapping("/productSalesCities/{city}")
    public List<CitySalesDTO> productSalesCities(@PathVariable String city)
    {
        return statisticsService.getProductsSoldInCitiesExceptOne(city);
    }

}
