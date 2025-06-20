package com.zlagoda.Zlagoda.dto.stats;

public class PromoOnlyCustomerDTO {
    private String cardNumber;
    private String surname;
    private String name;

    public PromoOnlyCustomerDTO(String cardNumber, String surname, String name) {
        this.cardNumber = cardNumber;
        this.surname = surname;
        this.name = name;
    }

    public PromoOnlyCustomerDTO() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }
}
