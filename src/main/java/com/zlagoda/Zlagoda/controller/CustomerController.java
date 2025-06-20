package com.zlagoda.Zlagoda.controller;

import com.zlagoda.Zlagoda.entity.CustomerCard;
import com.zlagoda.Zlagoda.service.implementation.CustomerCardServiceImpl;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerCardServiceImpl customerService;

    public CustomerController(CustomerCardServiceImpl customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public @ResponseBody List<CustomerCard> getCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/filter")
    public List<CustomerCard> filerCustomer(
            @RequestParam(required = false) Integer percentage,
            @RequestParam(required = false) String sortBy
    ) {
        List<String> sortParams = new ArrayList<>();
        if (sortBy != null && !sortBy.isBlank()) {
            sortParams.add(sortBy);
        }
        return customerService.filter(percentage, sortParams);
    }


    @GetMapping("/by-name/{name}")
    public @ResponseBody List<CustomerCard> getCustomersByName(@PathVariable String name)
    {
        return customerService.findByName(name);
    }

    @GetMapping("/{id}")
    public @ResponseBody CustomerCard getCustomerById(@PathVariable String id)
    {
        return customerService.findById(id);
    }

    @PostMapping()
    public void createCustomer(@RequestBody @Valid CustomerCard customer) {
        customerService.create(customer);
    }

    @PutMapping()
    public void editCustomer(@RequestBody @Valid CustomerCard customer) { customerService.update(customer); }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable String id) {
        customerService.delete(id);
    }

    @GetMapping("/search")
    public List<CustomerCard> search(@RequestParam("search") String query) {
        return customerService.findByName(query);
    }
}
