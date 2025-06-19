package com.zlagoda.Zlagoda.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public class CheckDTO {

    private String checkNumber;

    @JsonProperty("print_date")
    private String printDate;

    @JsonProperty("card_number")
    private String cardNumber;

    @JsonProperty("id_employee")
    private String idEmployee;

    @JsonProperty("sum_total")
    private BigDecimal sumTotal;

    @JsonProperty("vat")
    private BigDecimal vat;

    @JsonProperty("sales")
    private List<SaleDTO> sales;

    public CheckDTO() {
    }

    public CheckDTO(String checkNumber, String printDate, String cardNumber, String idEmployee, BigDecimal sumTotal, BigDecimal vat, List<SaleDTO> sales) {
        this.checkNumber = checkNumber;
        this.printDate = printDate;
        this.cardNumber = cardNumber;
        this.idEmployee = idEmployee;
        this.sumTotal = sumTotal;
        this.vat = vat;
        this.sales = sales;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getPrintDate() {
        return printDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public BigDecimal getSumTotal() {
        return sumTotal;
    }

    public void setSumTotal(BigDecimal sumTotal) {
        this.sumTotal = sumTotal;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public List<SaleDTO> getSales() {
        return sales;
    }

    public void setSales(List<SaleDTO> sales) {
        this.sales = sales;
    }
}

