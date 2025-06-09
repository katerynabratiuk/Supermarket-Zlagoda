package com.zlagoda.Zlagoda.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;


public class Employee {

    @NotNull
    @JsonProperty("id_employee")
    private String id;

    @NotNull
    @Size(min=1, max=50)
    @JsonProperty("empl_name")
    private String name;

    @NotNull
    @Size(min=1, max=50)
    @JsonProperty("empl_surname")
    private String surname;


    @JsonProperty("empl_patronymic")
    //@Size(min=1, max=50)
    private String patronymic;

    @NotNull
    @NotBlank
    @JsonProperty("empl_role")
    @Size(min=1, max=10)
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
    @Pattern(regexp = "(\\+)?[0-9]{9,12}")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotNull
    @Size(min=2, max=50)
    @Pattern(regexp = "[a-zA-Z-]{1,50}")
    @JsonProperty("city")
    private String city;

    @NotNull
    @Size(min=2, max=50)
    @JsonProperty("street")
    private String street;

    @NotNull
    @Size(min = 5, max = 5)
    @Pattern(regexp = "[0-9]{5}")
    @NotBlank
    @JsonProperty("zip_code")
    private String zipCode;

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

        @Override
        public Employee build() {
            return employee;
        }
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
                    String zipCode) {
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
                '}';
    }
}









