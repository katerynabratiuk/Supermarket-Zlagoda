package com.zlagoda.Zlagoda.repository.implementation;

import com.zlagoda.Zlagoda.entity.CustomerCard;
import com.zlagoda.Zlagoda.entity.Employee;
import com.zlagoda.Zlagoda.entity.Receipt;
import com.zlagoda.Zlagoda.exception.DataAccessException;
import com.zlagoda.Zlagoda.repository.CheckRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CheckRepositoryImpl implements CheckRepository {

    private static final String GET_ALL = "SELECT * FROM Receipt" ;
    private static final String CREATE = "INSERT INTO Receipt(check_number, id_employee, card_number, print_date, sum_total, vat) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String DELETE = "DELETE FROM Receipt WHERE check_number=?";

    private static final String FIND_BY_ID =
            "SELECT Receipt.*, " +
            "Employee.id_employee, Employee.empl_name, Employee.empl_surname, Employee.empl_patronymic, " +
            "Customer_card.card_number, Customer_card.cust_name, Customer_card.cust_surname, Customer_card.cust_patronymic " +
            "FROM Receipt "+
            "JOIN Employee ON Employee.id_employee = Receipt.id_employee " +
            "JOIN Customer_card ON Customer_card.card_number = Receipt.card_number " +
            "WHERE Receipt.check_number=?";


    private static final String FIND_DATE_BETWEEN =
            "SELECT Receipt.* " +
                    "FROM Employee " +
                    "JOIN Receipt ON Employee.id_employee = Receipt.id_employee " +
                    "WHERE print_date > ? AND print_date < ?";


    private static final String FIND_BY_EMPL_ID_AND_DATE_BETWEEN =
            "SELECT Receipt.* " +
                    "FROM Employee " +
                    "JOIN Receipt ON Employee.id_employee = Receipt.id_employee " +
                    "WHERE Employee.id_employee=? AND print_date > ? AND print_date < ?";



    private static final String FIND_SUM_BY_EMPLOYEE_ID_AND_DATE_BETWEEN = "SELECT SUM(sum_total) AS total" +
            "FROM Employee " +
            "JOIN Receipt ON Employee.id_employee = Receipt.id_employee " +
            "WHERE id_employee=? AND print_date>? AND print_date<?";

    private static final String FIND_TOTAL_SALES_SUM_BY_DATE_BETWEEN =
            "SELECT SUM(sum_total) AS total" +
                    "FROM Receipt " +
                    "WHERE print_date > ? AND print_date < ?";

    private static final String FIND_TOTAL_QUANTITY_SOLD_BY_PRODUCTID_AND_DATE_BETWEEN = "SELECT COUNT(*) AS total" +
            "FROM Sale JOIN Receipt ON Sale.check_number = Receipt.check_number " +
            "WHERE UPC=? AND print_date>? AND print_date<?";

    private static final String FIND_BY_EMPLOYEE_ID = "SELECT Receipt.*" +
            "FROM Employee INNER JOIN Receipt ON Employee.id_employee = Receipt.id_employee " +
            "WHERE id_employee=? AND print_date>? AND print_date<?";


    private final DBConnection dbConnection;

    public CheckRepositoryImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Receipt> findByEmployeeIdAndDateBetween(String employeeId, LocalDate from, LocalDate to) {
        List<Receipt> res = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_EMPL_ID_AND_DATE_BETWEEN)) {

            stmt.setString(1, employeeId);
            stmt.setDate(2, Date.valueOf(from));
            stmt.setDate(3, Date.valueOf(to));

            ResultSet resSet = stmt.executeQuery();

            while(resSet.next()) {
                Receipt receipt = extractCheckFromResultSet(resSet);
                res.add(receipt);
            }
            return res;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find the check", e);
        }
    }

    @Override
    public List<Receipt> findByDateBetween(LocalDate from, LocalDate to) {
        List<Receipt> res = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_DATE_BETWEEN)) {

            stmt.setDate(1, Date.valueOf(from));
            stmt.setDate(2, Date.valueOf(to)); // fixed here

            ResultSet resSet = stmt.executeQuery();

            while(resSet.next()) {
                Receipt receipt = extractCheckFromResultSet(resSet);
                res.add(receipt);
            }
            return res;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find the check", e);
        }
    }

    @Override
    public BigDecimal findSumByEmployeeIdAndDateBetween(String employeeId, LocalDate from, LocalDate to) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_SUM_BY_EMPLOYEE_ID_AND_DATE_BETWEEN)) {

            stmt.setString(1, employeeId);
            stmt.setDate(2, Date.valueOf(from));
            stmt.setDate(3, Date.valueOf(to));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("total");
            }
            return BigDecimal.ZERO;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to compute the sum", e);
        }
    }

    @Override
    public BigDecimal findTotalSalesSumByDateBetween(LocalDate from, LocalDate to) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_TOTAL_SALES_SUM_BY_DATE_BETWEEN)) {

            stmt.setDate(1, Date.valueOf(from));
            stmt.setDate(2, Date.valueOf(to));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("total");
            }
            return BigDecimal.ZERO;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to compute the sum", e);
        }
    }

    @Override
    public int findTotalQuantitySoldByProductIdAndDateBetween(String productId, LocalDate startDate, LocalDate endDate) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_TOTAL_QUANTITY_SOLD_BY_PRODUCTID_AND_DATE_BETWEEN)) {

            stmt.setString(1, productId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to compute the quantity", e);
        }
    }

    @Override
    public List<Receipt> findByEmployeeId(String employeeId) {
        List<Receipt> res = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_EMPLOYEE_ID)) {

            stmt.setString(1, employeeId);

            ResultSet resSet = stmt.executeQuery();

            while(resSet.next()) {
                Receipt receipt = extractCheckFromResultSet(resSet);
                res.add(receipt);
            }
            return res;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find the check", e);
        }
    }

    @Override
    public List<Receipt> findAll() {
        List<Receipt> receipts = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {

            while (rs.next()) {
                Receipt receipt = extractCheckFromResultSet(rs);
                receipts.add(receipt);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to get checks", e);
        }
        return receipts;
    }



    @Override
    public List<Receipt> findByName(String name) {
        return List.of();
    }

    @Override
    public Receipt findById(String checkNumber) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID)) {

            stmt.setString(1, checkNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Receipt receipt = extractCheckFromResultSet(rs);

                Employee empl = receipt.getEmployee();
                empl.setName(rs.getString("empl_name"));
                empl.setSurname(rs.getString("empl_surname"));
                empl.setPatronymic(rs.getString("empl_patronymic"));

                CustomerCard customerCard = receipt.getCard();
                customerCard.setName(rs.getString("cust_name"));
                customerCard.setSurname(rs.getString("cust_surname"));
                customerCard.setPatronymic(rs.getString("cust_patronymic"));

                return receipt;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find receipt by id", e);
        }
        return null;
    }

    @Transactional
    @Override
    public void create(Receipt receipt) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(CREATE)) {

            stmt.setString(1, receipt.getCheckNumber());
            stmt.setString(2, receipt.getEmployee().getId());

            if (receipt.getCard() != null) {
                stmt.setString(3, receipt.getCard().getCardNumber());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }
            stmt.setDate(4, Date.valueOf(receipt.getPrintDate()));
            stmt.setBigDecimal(5, receipt.getSumTotal());
            stmt.setBigDecimal(6, receipt.getVat());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Receipt receipt) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("UPDATE")) {

            stmt.setString(1, receipt.getEmployee().getId());

            if (receipt.getCard() != null) {
                stmt.setString(2, receipt.getCard().getCardNumber());
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }

            stmt.setDate(3, Date.valueOf(receipt.getPrintDate()));
            stmt.setBigDecimal(4, receipt.getSumTotal());
            stmt.setBigDecimal(5, receipt.getVat());
            stmt.setString(6, receipt.getCheckNumber());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(String checkNumber) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(DELETE)) {

            stmt.setString(1, checkNumber);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Receipt extractCheckFromResultSet(ResultSet rs) throws SQLException {
        Receipt receipt = new Receipt();
        receipt.setCheckNumber(rs.getString("check_number"));

        Employee employee = new Employee();
        employee.setId(rs.getString("id_employee"));
        receipt.setEmployee(employee);

        String cardNumber = rs.getString("card_number");
        if (cardNumber != null) {
            CustomerCard card = new CustomerCard();
            card.setCardNumber(cardNumber);
            receipt.setCard(card);
        }

        receipt.setPrintDate(rs.getDate("print_date").toLocalDate());
        receipt.setSumTotal(rs.getBigDecimal("sum_total"));



        return receipt;
    }


}
