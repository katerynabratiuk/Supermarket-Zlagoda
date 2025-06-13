package com.zlagoda.Zlagoda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.beans.Transient;

public class CustomerCard {

    @JsonProperty("card_number")
    private String cardNumber;

    @NotNull
    @Pattern(regexp = "^[A-Z][a-z]{1,49}(?:-[A-Z][a-z]{1,49})*$")
    @JsonProperty("cust_surname")
    private String surname;

    @NotNull
    @Pattern(regexp = "^[A-Z][a-z]{1,49}(?:-[A-Z][a-z]{1,49})*$")
    @JsonProperty("cust_name")
    private String name;

    @JsonProperty("cust_patronymic")
    private String patronymic;

    @NotNull
    //@Pattern(regexp = "(\\+)?[0-9]{10}")
    @JsonProperty("phone_number")
    private String phoneNumber;

    //@Size(min=2, max=50)
    //@Pattern(regexp = "^\\s*[a-zA-Z][0-9a-zA-Z][0-9a-zA-Z '-.=#/]*$\n")
    //@JsonProperty("city")
    private String city;

    //@Size(min=2, max=50)
    //@NotBlank
    //@JsonProperty("street")
    private String street;

    //@Size(min = 5, max = 5)
    //@NotBlank
    @JsonProperty("zip_code")
    private String zipCode;

    @Positive
    @Max(10)
    @JsonProperty("percent")
    private Integer percent;

    public CustomerCard() {
    }

    public static class Builder implements BuilderInterface<CustomerCard> {

        private final CustomerCard card;

        public Builder() {
            this.card = new CustomerCard();
        }

        public Builder setCardNumber(String cardNumber) {
            card.setCardNumber(cardNumber);
            return this;
        }

        public Builder setSurname(String surname) {
            card.setSurname(surname);
            return this;
        }

        public Builder setName(String name) {
            card.setName(name);
            return this;
        }

        public Builder setPatronymic(String patronymic) {
            card.setPatronymic(patronymic);
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            card.setPhoneNumber(phoneNumber);
            return this;
        }

        public Builder setCity(String city) {
            card.setCity(city);
            return this;
        }

        public Builder setStreet(String street) {
            card.setStreet(street);
            return this;
        }

        public Builder setZipCode(String zipCode) {
            card.setZipCode(zipCode);
            return this;
        }

        public Builder setPercent(Integer percent) {
            card.setPercent(percent);
            return this;
        }

        @Override
        public CustomerCard build() {
            return card;
        }
    }

    @AssertTrue()
    @Transient
    public boolean isPatronymicValid() {
        return patronymic == null || patronymic.isBlank()
                || patronymic.matches("^[A-Z][a-z]{1,49}(?:-[A-Z][a-z]{1,49})*$");
    }

    public CustomerCard(String cardNumber, String surname, String name, String phoneNumber, Integer percent) {
        this.cardNumber = cardNumber;
        this.surname = surname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.percent = percent;
    }

    public CustomerCard(String cardNumber, String surname, String name, String patronymic,
                        String phoneNumber, String city, String street, String zipCode, Integer percent) {
        this.cardNumber = cardNumber;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.percent = percent;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zip_code) {
        this.zipCode = zip_code;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

}
