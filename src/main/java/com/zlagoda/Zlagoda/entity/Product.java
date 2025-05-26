package com.zlagoda.Zlagoda.entity;

public class Product {

    private Integer id;
    private String name;
    private Category category;
    private String characteristics;


    public Product() {
    }
    public static class Builder implements BuilderInterface<Product> {

        private final Product product;

        public Builder() {
            this.product = new Product();
        }

        public Builder setId(Integer id) {
            product.setId(id);
            return this;
        }

        public Builder setName(String name) {
            product.setName(name);
            return this;
        }

        public Builder setCategory(Category category) {
            product.setCategory(category);
            return this;
        }

        public Builder setCharacteristics(String characteristics) {
            product.setCharacteristics(characteristics);
            return this;
        }

        @Override
        public Product build() {
            return product;
        }
    }


    public Product(Integer id, String name, Category category, String characteristics) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.characteristics = characteristics;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }
}
