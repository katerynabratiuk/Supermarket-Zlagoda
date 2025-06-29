package com.zlagoda.Zlagoda.repository.implementation;

import com.zlagoda.Zlagoda.entity.CustomerCard;
import com.zlagoda.Zlagoda.exception.DataAccessException;
import com.zlagoda.Zlagoda.repository.CustomerCardRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerCardRepositoryImpl implements CustomerCardRepository {

    private static final String GET_ALL = "SELECT * FROM Customer_card";
    private static final String GET_ALL_SORTED = "SELECT * FROM Customer_card";
    private static final String CREATE = "INSERT INTO Customer_card VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE = "UPDATE Customer_card SET cust_surname=?, cust_name=?, cust_patronymic=?, " +
            "phone_number=?, city=?, street=?, zip_code=?, percent=? WHERE card_number=?";
    private static final String DELETE = "DELETE FROM Customer_card WHERE card_number=?";
    private static final String FIND_BY_ID = "SELECT * FROM Customer_card WHERE card_number=?";
    private static final String FIND_BY_PERCENTAGE = "SELECT * FROM Customer_card WHERE percent=?";
    private static final String FIND_BY_SURNAME = "SELECT * FROM Customer_card WHERE cust_surname ILIKE ? OR cust_name ILIKE ? OR cust_patronymic ILIKE ?";

    private final DBConnection dbConnection;

    public CustomerCardRepositoryImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    private CustomerCard extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        return new CustomerCard(
                rs.getString("card_number"),
                rs.getString("cust_surname"),
                rs.getString("cust_name"),
                rs.getString("cust_patronymic"),
                rs.getString("phone_number"),
                rs.getString("city"),
                rs.getString("street"),
                rs.getString("zip_code"),
                rs.getInt("percent")
        );
    }

    @Override
    public List<CustomerCard> findAll() {
        List<CustomerCard> result = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(GET_ALL)) {

            while (rs.next()) {
                result.add(extractCustomerFromResultSet(rs));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find customer cards", e);
        }
        return result;
    }

    @Override
    public List<CustomerCard> findByName(String name) {
        List<CustomerCard> result = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_SURNAME)) {

            stmt.setString(1, name + '%');  // Prefix match
            stmt.setString(2, name + '%');
            stmt.setString(3, name + '%');
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(extractCustomerFromResultSet(rs));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find customer cards", e);
        }

        return result;
    }


    @Override
    public CustomerCard findById(String id) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractCustomerFromResultSet(rs);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find the customer card", e);
        }
        return null;
    }

    @Override
    public void create(CustomerCard card) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(CREATE)) {

            stmt.setString(1, card.getCardNumber());
            stmt.setString(2, card.getSurname());
            stmt.setString(3, card.getName());
            stmt.setString(4, card.getPatronymic());
            stmt.setString(5, card.getPhoneNumber());
            stmt.setString(6, card.getCity());
            stmt.setString(7, card.getStreet());
            stmt.setString(8, card.getZipCode());
            stmt.setInt(9, card.getPercent());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Failed to create the customer card", e);
        }
    }

    @Override
    public void update(CustomerCard card) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE)) {

            stmt.setString(1, card.getSurname());
            stmt.setString(2, card.getName());
            stmt.setString(3, card.getPatronymic());
            stmt.setString(4, card.getPhoneNumber());
            stmt.setString(5, card.getCity());
            stmt.setString(6, card.getStreet());
            stmt.setString(7, card.getZipCode());
            stmt.setInt(8, card.getPercent());
            stmt.setString(9, card.getCardNumber());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update the customer card", e);
        }
    }

    @Override
    public void delete(String cardNumber) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(DELETE)) {

            stmt.setString(1, cardNumber);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete the customer card", e);
        }
    }

    @Override
    public List<CustomerCard> filter(Integer percentage, List<String> sortParams) {
        StringBuilder query = new StringBuilder(GET_ALL);
        List<Object> parameters = new ArrayList<>();
        boolean hasWhere = false;

        if (percentage != null) {
            query.append(" WHERE percent = ?");
            parameters.add(percentage);
            hasWhere = true;
        }

        if (sortParams != null && !sortParams.isEmpty()) {
            query.append(" ORDER BY ");
            for (int i = 0; i < sortParams.size(); i++) {
                if (i > 0) query.append(", ");
                switch (sortParams.get(i)) {
                    case "name":
                        query.append("cust_surname, cust_name, cust_patronymic");
                        break;
                    default:
                        break;
                }
            }
        }

        List<CustomerCard> result = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(extractCustomerFromResultSet(rs));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to filter customer cards", e);
        }

        return result;
    }

}
