package com.zlagoda.Zlagoda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class Receipt {

    @NotNull
    @NotBlank
    @JsonProperty("check_number")
    private String checkNumber;

    @NotNull
    @JsonProperty("employee")
    private Employee employee;

    @JsonProperty("customer_card")
    private CustomerCard card;

    @NotNull
    @PastOrPresent
    @JsonProperty("print_date")
    private LocalDate printDate;

    @NotNull
    @Positive
    @DecimalMin(value = "0", inclusive = false)
    @JsonProperty("sum_total")
    private BigDecimal sumTotal;

    @NotNull
    @Positive
    @JsonProperty("vat")
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
