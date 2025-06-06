package com.zlagoda.Zlagoda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class Sale {

    @NotNull
    @JsonProperty("store_product")
    private StoreProduct storeProduct;

    @NotNull
    @JsonProperty("receipt")
    private Receipt receipt;

    @Positive()
    @Min(1)
    @JsonProperty("product_number")
    private Integer productNum;

    @DecimalMin(value="0", inclusive = false)
    @JsonProperty("selling_price")
    private BigDecimal selling_price;

    public Sale() {
    }
    public static class Builder implements BuilderInterface<Sale> {

        private final Sale sale;

        public Builder() {
            this.sale = new Sale();
        }

        public Builder setStoreProduct(StoreProduct storeProduct) {
            sale.setStoreProduct(storeProduct);
            return this;
        }

        public Builder setCheck(Receipt receipt) {
            sale.setCheck(receipt);
            return this;
        }

        public Builder setProductNum(Integer productNum) {
            sale.setProductNum(productNum);
            return this;
        }

        public Builder setSellingPrice(BigDecimal sellingPrice) {
            sale.setSelling_price(sellingPrice);
            return this;
        }

        @Override
        public Sale build() {
            return sale;
        }
    }


    public Sale(StoreProduct storeProduct, Receipt receipt, Integer productNum, BigDecimal selling_price) {
        this.storeProduct = storeProduct;
        this.receipt = receipt;
        this.productNum = productNum;
        this.selling_price = selling_price;
    }

    public StoreProduct getStoreProduct() {
        return storeProduct;
    }

    public void setStoreProduct(StoreProduct storeProduct) {
        this.storeProduct = storeProduct;
    }

    public Receipt getCheck() {
        return receipt;
    }

    public void setCheck(Receipt receipt) {
        this.receipt = receipt;
    }

    public Integer getProductNum() {
        return productNum;
    }

    public void setProductNum(Integer productNum) {
        this.productNum = productNum;
    }

    public BigDecimal getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(BigDecimal selling_price) {
        this.selling_price = selling_price;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "storeProduct=" + storeProduct +
                ", receipt=" + receipt +
                ", productNum=" + productNum +
                ", selling_price=" + selling_price +
                '}';
    }
}
