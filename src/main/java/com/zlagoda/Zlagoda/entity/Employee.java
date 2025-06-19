package com.zlagoda.Zlagoda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Employee {

    @JsonProperty("id_employee")
    @Size(max = 10, message = "ID cannot be more than 10 characters long!")
    private String id;

    @NotNull(message = "Name cannot be null!")
    @Size(max = 50, message = "Name cannot be more than 50 characters long!")
    @Pattern(regexp = "^[A-Z][a-z]{1,49}(?:-[A-Z][a-z]{1,49})*$",
             message = "Name does not meet requirements!")
    @JsonProperty("empl_name")
    private String name;

    @NotNull(message = "Surname cannot be null!")
    @Size(max = 50, message = "Surname cannot be more than 50 characters long!")
    @Pattern(regexp = "^[A-Z][a-z]{1,49}(?:-[A-Z][a-z]{1,49})*$",
            message = "Surname does not meet requirements!")
    @JsonProperty("empl_surname")
    private String surname;

    @JsonProperty("empl_patronymic")
    @Size(max = 50, message = "Patronymic cannot be more than 50 characters long!")
    private String patronymic;

    @NotNull(message = "Role cannot be null!")
    @NotBlank(message = "Role cannot be blank!")
    @JsonProperty("empl_role")
    private String role;

    @NotNull(message = "Salary cannot be null!")
    @Positive(message = "Salary must be a positive number!")
    @DecimalMax(value = "9999999999999.9999", message = "Salary must be less than or equal to 9999999999999.9999!")
    @JsonProperty("salary")
    private BigDecimal salary;

    @NotNull(message = "Date of birth cannot be null!")
    @Past(message = "Date of birth cannot be today or later than today!")
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull(message = "Date of start cannot be null!")
    @PastOrPresent(message = "Date of start cannot be later than today!")
    @JsonProperty("date_of_start")
    private LocalDate dateOfStart;

    @NotNull(message = "Phone number cannot be null!")
    @Pattern(regexp = "^\\+380\\d{9}$", message = "Number must start with +380 and consist of digits!")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotNull(message = "City cannot be null!")
    @Size(max=50, message = "City name must be between 2 and 50 characters!")
    @JsonProperty("city")
    private String city;

    @NotNull(message = "Street cannot be null!")
    @Size(max=50, message = "Street name must be between 2 and 50 characters!")
    @JsonProperty("street")
    private String street;

    @NotNull(message = "Zip code cannot be null!")
    @Size(min = 5, max = 9, message = "Zip code must be between 5 and 9 characters!")
    @Pattern(regexp = "^\\d+$", message = "Zip code must contain numbers only!")
    @NotBlank(message = "Zip code cannot be blank!")
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

    @AssertTrue(message = "Patronymic does not meet requirements!")
    public boolean isPatronymicValid() {
        return patronymic == null || patronymic.isBlank()
                || patronymic.matches("^[A-Z][a-z]{1,49}(?:-[A-Z][a-z]{1,49})*$");
    }

    @AssertTrue(message = "Employee has to be at least 18 years old!")
    public boolean isAdultAtStart() {
        if (dateOfBirth == null || dateOfStart == null) return true;
        return ChronoUnit.YEARS.between(dateOfBirth, dateOfStart) >= 18;
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

    public boolean getIsActive() {
        return Boolean.TRUE.equals(isActive);
    }

    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

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









