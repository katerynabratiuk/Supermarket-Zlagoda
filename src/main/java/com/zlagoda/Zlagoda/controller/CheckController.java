package com.zlagoda.Zlagoda.controller;

import com.zlagoda.Zlagoda.dto.CheckDTO;
import com.zlagoda.Zlagoda.dto.SaleDTO;
import com.zlagoda.Zlagoda.entity.*;
import com.zlagoda.Zlagoda.service.implementation.CheckServiceImpl;
import com.zlagoda.Zlagoda.util.IdGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
@RestController
@RequestMapping("/check")
public class CheckController {

    private final CheckServiceImpl checkService;
    private final IdGenerator idGenerator;

    public CheckController(CheckServiceImpl checkService, IdGenerator idGenerator) {
        this.checkService = checkService;
        this.idGenerator = idGenerator;
    }

    @GetMapping()
    public List<Receipt> getAll()
    {
        return checkService.findAll();
    }

    @GetMapping("/{checkId}")
    public Receipt getCheck(@PathVariable String checkId)
    {
        return checkService.findById(checkId);
    }

    @PostMapping()
    public ResponseEntity<?> createCheck(@RequestBody CheckDTO dto) {
        Receipt receipt = new Receipt();
        receipt.setCheckNumber(idGenerator.generate(IdGenerator.Option.Check));
        receipt.setPrintDate(LocalDate.parse(dto.getPrintDate()));
        receipt.setSumTotal(dto.getSumTotal());
        receipt.setVat(dto.getVat());

        Employee employee = new Employee();
        employee.setId(String.valueOf(dto.getIdEmployee()));
        receipt.setEmployee(employee);

        if (dto.getCardNumber() != null) {
            CustomerCard card = new CustomerCard();
            card.setCardNumber(dto.getCardNumber());
            receipt.setCard(card);
        }

        ArrayList<Sale> sales = new ArrayList<>();
        for (SaleDTO saleDto : dto.getSales()) {
            StoreProduct sp = new StoreProduct();
            sp.setUPC(saleDto.getUPC());
            sp.setSellingPrice(saleDto.getSellingPrice());

            Sale sale = new Sale();
            sale.setStoreProduct(sp);
            sale.setProductNum(saleDto.getQuantity());
            sale.setSelling_price(saleDto.getSellingPrice());
            sale.setCheck(receipt);

            sales.add(sale);
        }

        receipt.setSales(sales);
        //System.out.println(sales);
        checkService.create(receipt);

        return ResponseEntity.ok().build();
    }


}
