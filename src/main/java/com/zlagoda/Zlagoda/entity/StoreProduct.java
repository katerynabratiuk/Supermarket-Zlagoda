package com.zlagoda.Zlagoda.entity;

import java.math.BigDecimal;

public class StoreProduct {

    private String UPC;
    private String UPC_prom;
    private Product product;
    private BigDecimal sellingPrice;
    private Integer productsNumber;
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

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

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

}
