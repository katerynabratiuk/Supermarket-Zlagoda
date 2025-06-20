package com.zlagoda.Zlagoda.dto.check;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class SaleDTO {
    @JsonProperty("product_id")
    private String UPC;
    @JsonProperty("selling_price")
    private BigDecimal sellingPrice;
    @JsonProperty("quantity")
    private int quantity;

    public SaleDTO() {
    }

    public SaleDTO(String productId, BigDecimal sellingPrice, int quantity) {
        this.UPC = productId;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
    }

    public String getUPC() {
        return UPC;
    }

    public void setUPC(String UPC) {
        this.UPC = UPC;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

