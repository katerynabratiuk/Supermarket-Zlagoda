package com.zlagoda.Zlagoda.dto.stats;

public class EmployeeCategorySalesDTO {

    private String idEmployee;
    private String emplSurname;
    private Integer categoryNumber;
    private String categoryName;
    private int totalProductsSold;

    public EmployeeCategorySalesDTO() {
    }

    public EmployeeCategorySalesDTO(String idEmployee, String emplSurname, Integer categoryNumber, String categoryName, Integer categoryId, int totalProductsSold) {
        this.idEmployee = idEmployee;
        this.emplSurname = emplSurname;
        this.categoryNumber = categoryNumber;
        this.categoryName = categoryName;
        this.totalProductsSold = totalProductsSold;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getEmplSurname() {
        return emplSurname;
    }

    public void setEmplSurname(String emplSurname) {
        this.emplSurname = emplSurname;
    }

    public Integer getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(Integer categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public int getTotalProductsSold() {
        return totalProductsSold;
    }

    public void setTotalProductsSold(int totalProductsSold) {
        this.totalProductsSold = totalProductsSold;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

