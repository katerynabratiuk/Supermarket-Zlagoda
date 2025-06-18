package com.zlagoda.Zlagoda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class StoreProduct {

    @NotNull(message = "UPC cannot be null!")
    @Size(max = 12, message = "UPC cannot be more then 12 characters long!")
    @JsonProperty("UPC")
    private String UPC;

    @JsonProperty("UPC_prom")
    @Size(max = 12, message = "UPC cannot be more then 12 characters long!")
    private String UPC_prom;

    @NotNull(message = "Product cannot be null!")
    @JsonProperty("product")
    private Product product;

    @DecimalMin(value="0", inclusive = false)
    @DecimalMax(value="999999999.9999", message = "Selling price cannot be more than 999999999999.9999")
    @JsonProperty("selling_price")
    private BigDecimal sellingPrice;

    @DecimalMin(value="0", inclusive = false)
    @JsonProperty("new_price")
    private BigDecimal newPrice;

    @Min(value = 1, message = "There must be at least one product!")
    @JsonProperty("products_number")
    private Integer productsNumber;

    @NotNull()
    @JsonProperty("promotional_product")
    private boolean isPromotional;

    public StoreProduct() {
    }

    public static class Builder implements BuilderInterface<StoreProduct> {

        private final StoreProduct storeProduct;

        public Builder() {
            this.storeProduct = new StoreProduct();
        }

        public Builder setUPC(String UPC) {
            storeProduct.setUPC(UPC);
            return this;
        }

        public Builder setUPC_prom(String UPC_prom) {
            storeProduct.setUPC_prom(UPC_prom);
            return this;
        }

        public Builder setProduct(Product product) {
            storeProduct.setProduct(product);
            return this;
        }

        public Builder setSellingPrice(BigDecimal sellingPrice) {
            storeProduct.setSellingPrice(sellingPrice);
            return this;
        }

        public Builder setNewPrice(BigDecimal sellingPrice) {
            storeProduct.setNewPrice(sellingPrice);
            return this;
        }

        public Builder setProductsNumber(Integer productsNumber) {
            storeProduct.setProductsNumber(productsNumber);
            return this;
        }

        public Builder setPromotional(boolean isPromotional) {
            storeProduct.setPromotional(isPromotional);
            return this;
        }



        @Override
        public StoreProduct build() {
            return storeProduct;
        }
    }


    public StoreProduct(String UPC, String UPC_prom, Product product, BigDecimal sellingPrice, Integer productsNumber, boolean isPromotional) {
        this.UPC = UPC;
        this.UPC_prom = UPC_prom;
        this.product = product;
        this.sellingPrice = sellingPrice;
        this.productsNumber = productsNumber;
        this.isPromotional = isPromotional;
    }

    public String getUPC() {
        return UPC;
    }

    public void setUPC(String UPC) {
        this.UPC = UPC;
    }

    public String getUPC_prom() {
        return UPC_prom;
    }

    public void setUPC_prom(String UPC_prom) {
        this.UPC_prom = UPC_prom;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setNewPrice(BigDecimal newPrice) { this.newPrice = newPrice; }

    public Integer getProductsNumber() {
        return productsNumber;
    }

    public void setProductsNumber(Integer productsNumber) {
        this.productsNumber = productsNumber;
    }

    public boolean isPromotional() {
        return isPromotional;
    }

    public void setPromotional(boolean promotional) {
        isPromotional = promotional;
    }

    @Override
    public String toString() {
        return "StoreProduct{" +
                "UPC='" + UPC + '\'' +
                ", UPC_prom='" + UPC_prom + '\'' +
                ", product=" + product +
                ", sellingPrice=" + sellingPrice +
                ", productsNumber=" + productsNumber +
                ", isPromotional=" + isPromotional +
                '}';
    }
}
