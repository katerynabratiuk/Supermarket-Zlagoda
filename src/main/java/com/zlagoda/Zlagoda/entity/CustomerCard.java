package com.zlagoda.Zlagoda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.beans.Transient;

public class CustomerCard {

    @JsonProperty("card_number")
    private String cardNumber;

    @NotNull
    @Pattern(regexp = "^[A-Z][a-z]{1,49}(?:-[A-Z][a-z]{1,49})*$",
            message = "Surname does not meet requirements!")
    @Size(max = 50, message = "Surname cannot be more than 50 characters!")
    @JsonProperty("cust_surname")
    private String surname;

    @NotNull
    @Pattern(regexp = "^[A-Z][a-z]{1,49}(?:-[A-Z][a-z]{1,49})*$",
            message = "Name does not meet requirements!")
    @Size(max = 50, message = "Name cannot be more than 50 characters!")
    @JsonProperty("cust_name")
    private String name;

    @JsonProperty("cust_patronymic")
    @Size(max = 50, message = "Patronymic cannot be more than 50 characters!")
    private String patronymic;

    @NotNull
    @Pattern(regexp = "^\\+380\\d{9}$", message = "Number must start with +380 and consist of digits!")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotNull(message = "City cannot be null!")
    @Size(max=50, message = "City must be between 2 and 50 characters!")
    //@JsonProperty("city")
    private String city;

    @NotNull(message = "Street cannot be null!")
    @Size(max=50)
    //@JsonProperty("street")
    private String street;

    @NotNull(message = "Zip code cannot be null!")
    @Size(max = 9, message = "Zip code cannot be more than 9 characters long!")
    @JsonProperty("zip_code")
    private String zipCode;

    @PositiveOrZero(message = "Discount cannot be negative!")
    @Max(value = 15, message = "Max percent is 15!")
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
    public boolean isPatronymicValid() {
        return patronymic == null || patronymic.isBlank()
                || patronymic.matches("^[A-Z][a-z]{1,49}(?:-[A-Z][a-z]{1,49})*$");
    }

    @AssertTrue()
    public boolean isZipCodeValid() {
        return zipCode == null || zipCode.isBlank()
                || zipCode.matches("^[0-9]{5,9}$");
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
