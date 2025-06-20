package com.zlagoda.Zlagoda.controller;

import com.zlagoda.Zlagoda.dto.stats.EmployeeCategorySalesDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatisticsController {

    @GetMapping("/productsSoldByEmployee")
    public List<EmployeeCategorySalesDTO> productsSoldByEmployee()
    {
        return List.of();
    }

}
