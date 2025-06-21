package com.zlagoda.Zlagoda.controller;

import com.zlagoda.Zlagoda.dto.stats.CitySalesDTO;
import com.zlagoda.Zlagoda.dto.stats.EmployeeDTO;
import com.zlagoda.Zlagoda.dto.stats.ProductSaleDTO;
import com.zlagoda.Zlagoda.dto.stats.PromoOnlyCustomerDTO;
import com.zlagoda.Zlagoda.entity.CustomerCard;
import com.zlagoda.Zlagoda.entity.Employee;
import com.zlagoda.Zlagoda.service.implementation.StatisticsServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/stats")
@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
public class StatisticsController {

    private final StatisticsServiceImpl statisticsService;

    public StatisticsController(StatisticsServiceImpl statisticsService) {
        this.statisticsService = statisticsService;
    }


    @GetMapping("/productsSoldByEmployee/{categoryID}")
    public List<EmployeeDTO> productsSoldByEmployee(@PathVariable Integer categoryID)
    {
        return statisticsService.productsSoldByEmployeeByCategory(categoryID);
    }

    @GetMapping("/productSalesCities/{city}")
    public List<CitySalesDTO> productSalesCities(@PathVariable String city)
    {
        return statisticsService.getProductsSoldInCitiesExceptOne(city);
    }

    @GetMapping("/promoCustomers")
    public List<PromoOnlyCustomerDTO> promoCustomers()
    {
        return statisticsService.getPromoCustomers();
    }

    @GetMapping("/loyalCustomers")
    public List<CustomerCard> getLoyalCustomers()
    {
        return statisticsService.getLoyalCustomers();
    }

    @GetMapping("/employeeNeverSoldCategory/{categoryID}")
    public List<Employee> getEmployeesWhoNeverSoldCategory(@PathVariable Integer categoryID)
    {
        return statisticsService.getEmployeesWhoNeverSoldCategory(categoryID);
    }

    @GetMapping("/sold-units")
    public ProductSaleDTO getTotalUnitsSoldForProductInPeriod(
            @RequestParam() LocalDate from,
            @RequestParam() LocalDate to,
            @RequestParam() String UPC
    )
    {
        return statisticsService.getTotalUnitsSoldForProductInPeriod(UPC, from, to);
    }

}
