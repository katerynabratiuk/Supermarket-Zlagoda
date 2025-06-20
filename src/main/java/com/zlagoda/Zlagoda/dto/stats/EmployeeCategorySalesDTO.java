package com.zlagoda.Zlagoda.dto.stats;

public class EmployeeCategorySalesDTO {

    private String idEmployee;
    private String emplSurname;
    private String categoryName;
    private int totalProductsSold;

    public EmployeeCategorySalesDTO(String idEmployee, String emplSurname, String categoryName, int totalProductsSold) {
        this.idEmployee = idEmployee;
        this.emplSurname = emplSurname;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getTotalProductsSold() {
        return totalProductsSold;
    }

    public void setTotalProductsSold(int totalProductsSold) {
        this.totalProductsSold = totalProductsSold;
    }
}

