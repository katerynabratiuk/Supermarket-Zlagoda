package com.zlagoda.Zlagoda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Category{

    //@NotNull
    @JsonProperty("category_number")
    private Integer number;

    @JsonProperty("category_name")
    @NotNull(message = "Category name cannot be null!")
    @Size(max = 50, message = "Category name cannot be more than 50 characters long!")
    private String name;

    public Category() {
    }

    public static class Builder implements BuilderInterface<Category>{


        Category category = new Category();

        public Builder setId(Integer number) {
            category.number = number;
            return this;
        }

        public Builder setName(String name) {
            category.name = name;
            return this;
        }
        public Category build()
        {
            return category;
        }

    }

    public Category(Integer number, String name) {
        this.number = number;
        this.name = name;
    }

    public Category(String name) {
        this.name = name;
    }


    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "number=" + number +
                ", name='" + name + '\'' +
                '}';
    }
}
