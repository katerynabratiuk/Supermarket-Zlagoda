package com.zlagoda.Zlagoda.controller;

import com.zlagoda.Zlagoda.entity.Receipt;
import com.zlagoda.Zlagoda.service.CheckService;
import com.zlagoda.Zlagoda.service.implementation.CheckServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/check")
public class CheckController {

    private CheckServiceImpl checkService;

}
