package com.zlagoda.Zlagoda.entity;

public class CustomerCard {

    private String cardNumber;
    private String surname;
    private String name;
    private String patronymic;
    private String phoneNumber;
    private String city;
    private String street;
    private String zip_code;
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

    public CustomerCard(String cardNumber, String surname, String name, String phoneNumber, Integer percent) {
        this.cardNumber = cardNumber;
        this.surname = surname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.percent = percent;
    }

    public CustomerCard(String cardNumber, String surname, String name, String patronymic,
                        String phoneNumber, String city, String street, String zip_code, Integer percent) {
        this.cardNumber = cardNumber;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.street = street;
        this.zip_code = zip_code;
        this.percent = percent;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public String getZipCode() {
        return zip_code;
    }

    public void setZipCode(String zip_code) {
        this.zip_code = zip_code;
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
