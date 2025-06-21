package com.zlagoda.Zlagoda.dto.stats;

public class ProductSaleDTO {

    private Integer amount;

    public ProductSaleDTO() {
    }

    public ProductSaleDTO(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
