package com.zlagoda.Zlagoda.controller;

import com.zlagoda.Zlagoda.entity.Receipt;
import com.zlagoda.Zlagoda.service.CheckService;
import com.zlagoda.Zlagoda.service.implementation.CheckServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
@RestController
@RequestMapping("/check")
public class CheckController {

    private final CheckServiceImpl checkService;

    public CheckController(CheckServiceImpl checkService) {
        this.checkService = checkService;
    }

    @GetMapping()
    public List<Receipt> getAll()
    {
        return checkService.findAll();
    }

}
