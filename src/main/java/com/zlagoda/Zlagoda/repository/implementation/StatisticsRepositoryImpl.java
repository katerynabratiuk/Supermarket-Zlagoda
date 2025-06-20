package com.zlagoda.Zlagoda.repository.implementation;

import com.zlagoda.Zlagoda.dto.stats.CitySalesDTO;
import com.zlagoda.Zlagoda.dto.stats.EmployeeCategorySalesDTO;
import com.zlagoda.Zlagoda.dto.stats.PromoOnlyCustomerDTO;
import com.zlagoda.Zlagoda.repository.StatisticsRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatisticsRepositoryImpl implements StatisticsRepository {

    private final String GET_PRODUCTS_SOLD_BY_EMPLOYEE_BY_CATEGORY =
            "SELECT " +
            "    e.id_employee, " +
            "    e.empl_surname, " +
            "    cat.category_name, " +
            "    cat.category_number, " +
            "    SUM(s.product_number) AS total_products_sold " +
            "FROM employee e " +
            "INNER JOIN receipt ch ON e.id_employee = ch.id_employee " +
            "INNER JOIN sale s ON ch.check_number = s.check_number " +
            "INNER JOIN store_product sp ON s.upc = sp.upc " +
            "INNER JOIN product p ON sp.id_product = p.id_product " +
            "INNER JOIN category cat ON p.category_number = cat.category_number " +
            "WHERE cat.category_number = ? " +
            "GROUP BY e.id_employee, e.empl_surname, cat.category_name, cat.category_number  " +
            "ORDER BY total_products_sold DESC";

    private final String GET_PRODUCTS_SOLD_IN_CITIES_EXCEPT_ONE = "SELECT Customer_Card.city, SUM(Sale.product_number) AS total_amount " +
            "FROM Sale " +
            "INNER JOIN Receipt ON Sale.check_number = Receipt.check_number " +
            "INNER JOIN Customer_Card ON Receipt.card_number = Customer_Card.card_number " +
            "WHERE Customer_Card.city != ? " +
            "GROUP BY Customer_Card.city " +
            "ORDER BY total_amount DESC; ";

    private final String GET_PROMO_ONLY_CUSTOMERS = "SELECT DISTINCT Customer_Card.card_number, Customer_Card.cust_surname, Customer_Card.cust_name\n" +
            "FROM Customer_Card\n" +
            "WHERE NOT EXISTS (\n" +
            "SELECT 1\n" +
            "FROM Receipt\n" +
            "WHERE Receipt.card_number = Customer_Card.card_number\n" +
            "AND NOT EXISTS (\n" +
            "SELECT 1\n" +
            "FROM Sale\n" +
            "INNER JOIN Store_Product ON Sale.UPC = Store_Product.UPC\n" +
            "WHERE Sale.check_number = Receipt.check_number\n" +
            "AND Store_Product.promotional_product = True ))";

    private final DBConnection dbConnection;


    public StatisticsRepositoryImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }


    @Override
    public List<EmployeeCategorySalesDTO> productsSoldByEmployeeByCategory(Integer categoryNumber) {
        List<EmployeeCategorySalesDTO> res = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_PRODUCTS_SOLD_BY_EMPLOYEE_BY_CATEGORY)
        ) {
            stmt.setInt(1, categoryNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EmployeeCategorySalesDTO dto = new EmployeeCategorySalesDTO();
                    dto.setIdEmployee(rs.getString("id_employee"));
                    dto.setEmplSurname(rs.getString("empl_surname"));
                    dto.setCategoryName(rs.getString("category_name"));
                    dto.setCategoryNumber(rs.getInt("category_number"));
                    dto.setTotalProductsSold(rs.getInt("total_products_sold"));
                    res.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public List<CitySalesDTO> getProductsSoldInCitiesExceptOne(String city) {
        List<CitySalesDTO> res = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_PRODUCTS_SOLD_IN_CITIES_EXCEPT_ONE)
        ) {
            stmt.setString(1, city);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CitySalesDTO dto = new CitySalesDTO
                    (
                            rs.getString("city"),
                            rs.getInt("total_amount")
                    );
                    res.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public List<PromoOnlyCustomerDTO> getPromoCustomers() {
        List<PromoOnlyCustomerDTO> res = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            ResultSet rs = stmt.executeQuery(GET_PROMO_ONLY_CUSTOMERS);
            while (rs.next()) {
                PromoOnlyCustomerDTO dto = new PromoOnlyCustomerDTO();
                dto.setSurname(rs.getString("cust_surname"));
                dto.setName(rs.getString("cust_name"));
                dto.setCardNumber(rs.getString("card_number"));
                res.add(dto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }


}
