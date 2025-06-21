package com.zlagoda.Zlagoda.repository.implementation;

import com.zlagoda.Zlagoda.dto.stats.CitySalesDTO;
import com.zlagoda.Zlagoda.dto.stats.EmployeeDTO;
import com.zlagoda.Zlagoda.dto.stats.ProductSaleDTO;
import com.zlagoda.Zlagoda.dto.stats.PromoOnlyCustomerDTO;
import com.zlagoda.Zlagoda.entity.CustomerCard;
import com.zlagoda.Zlagoda.entity.Employee;
import com.zlagoda.Zlagoda.repository.StatisticsRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
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

    private final String GET_LOYAL_CUSTOMERS =
            "SELECT cc.card_number, cc.cust_surname, cc.cust_name\n" +
            "FROM customer_card cc\n" +
            "WHERE NOT EXISTS (\n" +
            "    SELECT cat.category_number\n" +
            "    FROM category cat\n" +
            "    WHERE NOT EXISTS (\n" +
            "        SELECT *\n" +
            "        FROM receipt ch\n" +
            "        INNER JOIN sale s ON ch.check_number = s.check_number\n" +
            "        INNER JOIN store_product sp ON s.upc = sp.upc\n" +
            "        INNER JOIN product p ON sp.id_product = p.id_product\n" +
            "        WHERE \n" +
            "            ch.card_number = cc.card_number\n" +
            "            AND p.category_number = cat.category_number\n" +
            "    )\n" +
            ");";

    private final String GET_EMPLOYEES_WHO_NEVER_SOLD_CATEGORY =
            "SELECT DISTINCT Employee.id_employee, Employee.empl_surname, Employee.empl_name\n" +
            "FROM Employee\n" +
            "WHERE NOT EXISTS (\n" +
            "    SELECT 1\n" +
            "    FROM Receipt\n" +
            "    WHERE Receipt.id_employee = Employee.id_employee\n" +
            "      AND NOT EXISTS (\n" +
            "        SELECT 1\n" +
            "        FROM Sale\n" +
            "        JOIN Store_Product ON Sale.UPC = Store_Product.UPC\n" +
            "        JOIN Product ON Store_Product.id_product = Product.id_product\n" +
            "        WHERE Sale.check_number = Receipt.check_number\n" +
            "          AND Product.category_number = ?\n" +
            "      )\n" +
            ");";

    private final String GET_TOTAL_UNITS_SOLD_FOR_PRODUCT_IN_PERIOD =
            "SELECT COALESCE(SUM(product_number), 0) AS total_sum " +
                    "FROM Sale " +
                    "JOIN Receipt ON Sale.check_number = Receipt.check_number " +
                    "WHERE UPC = ? " +
                    "AND print_date >= ? " +
                    "AND print_date <= ?;";

    private final DBConnection dbConnection;


    public StatisticsRepositoryImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }


    @Override
    public List<EmployeeDTO> productsSoldByEmployeeByCategory(Integer categoryNumber) {
        List<EmployeeDTO> res = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_PRODUCTS_SOLD_BY_EMPLOYEE_BY_CATEGORY)
        ) {
            stmt.setInt(1, categoryNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EmployeeDTO dto = new EmployeeDTO();
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

    @Override
    public List<Employee> getEmployeesWhoNeverSoldCategory(Integer categoryID) {
        List<Employee> res = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_EMPLOYEES_WHO_NEVER_SOLD_CATEGORY)
        ) {
            stmt.setInt(1, categoryID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee empl = new Employee();
                    empl.setSurname(rs.getString("empl_surname"));
                    empl.setName(rs.getString("empl_name"));
                    empl.setId(rs.getString("id_employee"));
                    res.add(empl);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public ProductSaleDTO getTotalUnitsSoldForProductInPeriod(String upc, LocalDate from, LocalDate to) {
        try(Connection connection = dbConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(GET_TOTAL_UNITS_SOLD_FOR_PRODUCT_IN_PERIOD)
        ){


            stmt.setString(1, upc);
            stmt.setDate(2, Date.valueOf(from));
            stmt.setDate(3, Date.valueOf(to));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                   return new ProductSaleDTO(rs.getInt("total_sum"));
                }

            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public List<CustomerCard> getLoyalCustomers() {

        List<CustomerCard> res = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             Statement stmt = connection.createStatement();
        ) {
            ResultSet rs = stmt.executeQuery(GET_LOYAL_CUSTOMERS);
            while (rs.next()) {
                CustomerCard customer = new CustomerCard();
                customer.setSurname(rs.getString("cust_surname"));
                customer.setName(rs.getString("cust_name"));
                customer.setCardNumber(rs.getString("card_number"));
                res.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;

    }
}
