package com.zlagoda.Zlagoda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class Receipt {

    @NotNull(message = "Check number cannot be null!")
    @NotBlank(message = "Check number cannot be blank!")
    @JsonProperty("check_number")
    private String checkNumber;

    @NotNull(message = "Employee cannot be null!")
    @JsonProperty("employee")
    private Employee employee;

    @JsonProperty("customer_card")
    private CustomerCard card;

    @NotNull(message = "Date of print cannot be null!")
    @PastOrPresent(message = "Date of print cannot be later than today!")
    @JsonProperty("print_date")
    private LocalDate printDate;

    @NotNull(message = "Total sum cannot be null!")
    @Positive(message = "Total sum must be a positive value!")
    @DecimalMin(value = "0", inclusive = false)
    @DecimalMax(value = "9999999999999.9999", message = "Total sum cannot be more than 999999999999.9999")
    @JsonProperty("sum_total")
    private BigDecimal sumTotal;

    @NotNull(message = "VAT cannot be null!")
    @Positive(message = "VAT must be a positive value!")
    @JsonProperty("vat")
    @DecimalMax(value = "9999999999999.9999", message = "VAT cannot be more than 999999999999.9999")
    private BigDecimal vat;

    @NotNull
    @Size(min = 1)
    @JsonProperty("products")
    private ArrayList<StoreProduct> products = new ArrayList<>();

    public Receipt() {}

    public static class Builder implements BuilderInterface<Receipt> {

        private final Receipt receipt;

        public Builder() {
            this.receipt = new Receipt();
        }

        public Builder setCheckNumber(String checkNumber) {
            receipt.setCheckNumber(checkNumber);
            return this;
        }

        public Builder setEmployee(Employee employee) {
            receipt.setEmployee(employee);
            return this;
        }

        public Builder setCard(CustomerCard card) {
            receipt.setCard(card);
            return this;
        }

        public Builder setPrintDate(LocalDate printDate) {
            receipt.setPrintDate(printDate);
            return this;
        }

        public Builder setSumTotal(BigDecimal sumTotal) {
            receipt.setSumTotal(sumTotal);
            return this;
        }

        public Builder addProduct(StoreProduct product) {
            receipt.addProduct(product);
            return this;
        }

        @Override
        public Receipt build() {
            return receipt;
        }
    }

    public Receipt(String checkNumber, Employee employee, CustomerCard card, LocalDate printDate, BigDecimal sumTotal) {
        this.checkNumber = checkNumber;
        this.employee = employee;
        this.card = card;
        this.printDate = printDate;
        this.sumTotal = sumTotal;
        this.vat = sumTotal.multiply(BigDecimal.valueOf(0.2));
        this.products = new ArrayList<>();
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public CustomerCard getCard() {
        return card;
    }

    public void setCard(CustomerCard card) {
        this.card = card;
    }

    public LocalDate getPrintDate() {
        return printDate;
    }

    public void setPrintDate(LocalDate printDate) {
        this.printDate = printDate;
    }

    public BigDecimal getSumTotal() {
        return sumTotal;
    }

    public void setSumTotal(BigDecimal sumTotal) {
        this.sumTotal = sumTotal;
        this.vat = sumTotal != null ? sumTotal.multiply(BigDecimal.valueOf(0.2)) : null;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void addProduct(StoreProduct sp) {
        products.add(sp);
    }

    public ArrayList<StoreProduct> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "checkNumber='" + checkNumber + '\'' +
                ", employee=" + employee +
                ", card=" + card +
                ", printDate=" + printDate +
                ", sumTotal=" + sumTotal +
                ", vat=" + vat +
                ", products=" + products +
                '}';
    }
}
