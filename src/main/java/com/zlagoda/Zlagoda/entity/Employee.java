package com.zlagoda.Zlagoda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Employee {

    @JsonProperty("id_employee")
    private String id;

    @NotNull
    @Pattern(regexp = "^[A-Z][a-z]{1,49}(?:-[A-Z][a-z]{1,49})*$")
    @JsonProperty("empl_name")
    private String name;

    @NotNull
    @Pattern(regexp = "^[A-Z][a-z]{1,49}(?:-[A-Z][a-z]{1,49})*$")
    @JsonProperty("empl_surname")
    private String surname;

    @JsonProperty("empl_patronymic")
    private String patronymic;

    @NotNull
    @NotBlank
    @JsonProperty("empl_role")
    private String role;

    @NotNull
    @Positive
    @JsonProperty("salary")
    private BigDecimal salary;

    @NotNull
    @Past
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull
    @PastOrPresent
    @JsonProperty("date_of_start")
    private LocalDate dateOfStart;

    @NotNull
    //@Pattern(regexp = "(\\+)?[0-9]{10}")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotNull
    @Size(min=2, max=50)
    //@Pattern(regexp = "^\\s*[a-zA-Z][0-9a-zA-Z][0-9a-zA-Z '-.=#/]*$\n")
    @JsonProperty("city")
    private String city;

    @NotNull
    @Size(min=2, max=50)
    //@Pattern(regexp = "^\\s*[a-zA-Z][0-9a-zA-Z][0-9a-zA-Z '-.=#/]*$\n")
    @JsonProperty("street")
    private String street;

    @NotNull
    @Size(min = 5, max = 5)
    //@Pattern(regexp = "[0-9]")
    @NotBlank
    @JsonProperty("zip_code")
    private String zipCode;

    private String empl_username;

    @JsonProperty("is_active")
    private Boolean isActive;

    public Employee(){};

    public static class Builder implements BuilderInterface<Employee> {

        private final Employee employee;

        public Builder() {
            this.employee = new Employee();
        }

        public Builder setId(String id) {
            employee.setId(id);
            return this;
        }

        public Builder setName(String name) {
            employee.setName(name);
            return this;
        }

        public Builder setSurname(String surname) {
            employee.setSurname(surname);
            return this;
        }

        public Builder setPatronymic(String patronymic) {
            employee.setPatronymic(patronymic);
            return this;
        }

        public Builder setRole(String role) {
            employee.setRole(role);
            return this;
        }

        public Builder setSalary(BigDecimal salary) {
            employee.setSalary(salary);
            return this;
        }

        public Builder setDateOfBirth(LocalDate dateOfBirth) {
            employee.setDateOfBirth(dateOfBirth);
            return this;
        }

        public Builder setDateOfStart(LocalDate dateOfStart) {
            employee.setDateOfStart(dateOfStart);
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            employee.setPhoneNumber(phoneNumber);
            return this;
        }

        public Builder setCity(String city) {
            employee.setCity(city);
            return this;
        }

        public Builder setStreet(String street) {
            employee.setStreet(street);
            return this;
        }

        public Builder setZipCode(String zipCode) {
            employee.setZipCode(zipCode);
            return this;
        }

        public Builder setEmplUsername(String empl_username) {
            employee.setEmplUsername(empl_username);
            return this;
        }

        public Builder setIsActive(boolean isActive) {
            employee.setIsActive(isActive);
            return this;
        }

        @Override
        public Employee build() {
            return employee;
        }
    }

    @AssertTrue()
    @Transient
    public boolean isPatronymicValid() {
        return patronymic == null || patronymic.isBlank()
                || patronymic.matches("^[A-Z][a-z]{1,49}(?:-[A-Z][a-z]{1,49})*$");
    }

    public Employee(String id,
                    String name,
                    String surname,
                    String patronymic,
                    String role,
                    BigDecimal salary,
                    LocalDate dateOfBirth,
                    LocalDate dateOfStart,
                    String phoneNumber,
                    String city,
                    String street,
                    String zipCode,
                    String empl_username,
                    boolean isActive) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.role = role;
        this.salary = salary;
        this.dateOfBirth = dateOfBirth;
        this.dateOfStart = dateOfStart;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.empl_username = empl_username;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfStart() {
        return dateOfStart;
    }

    public void setDateOfStart(LocalDate dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getEmplUsername() { return empl_username; }

    public void setEmplUsername(String empl_username) { this.empl_username = empl_username; }

    public boolean getIsActive() { return isActive; }

    public void setIsActive(boolean isActive) { this.isActive = isActive; }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", role='" + role + '\'' +
                ", salary=" + salary +
                ", dateOfBirth=" + dateOfBirth +
                ", dateOfStart=" + dateOfStart +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", username='" + empl_username + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}









