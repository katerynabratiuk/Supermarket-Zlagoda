package com.zlagoda.Zlagoda.dto.stats;

public class CitySalesDTO {
    private String city;
    private int totalAmount;

    public CitySalesDTO(String city, int totalAmount) {
        this.city = city;
        this.totalAmount = totalAmount;
    }

    public CitySalesDTO() {

    }

    public String getCity() {
        return city;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}
