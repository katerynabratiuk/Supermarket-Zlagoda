package com.zlagoda.Zlagoda.util;

import com.zlagoda.Zlagoda.repository.implementation.DBConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
@Component
public class IdGenerator {

    private final String EMPLOYEE = "SELECT MAX(CAST(id_employee AS INTEGER)) FROM employee";
    private final String PRODUCT = "SELECT MAX(CAST(UPC AS INTEGER)) FROM Store_Product";
    private final String CHECK = "SELECT MAX(CAST(check_number AS INTEGER)) FROM Receipt";

    private final DBConnection dbConnection;

    @Autowired
    public IdGenerator(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public enum Option {Employee, Product, Check}

    public String generate(Option option) {
        String query;
        int attributeLength = switch (option) {
            case Employee -> {
                query = EMPLOYEE;
                yield 10;
            }
            case Product -> {
                query = PRODUCT;
                yield 12;
            }
            case Check -> {
                query = CHECK;
                yield 10;
            }
            default -> throw new RuntimeException("No such option");
        };

        try (Connection connection = dbConnection.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(query);

            int maxId = 0;
            if (resultSet.next()) {
                maxId = resultSet.getInt(1);
            }
            int newId = maxId + 1;
            return String.format("%0"+ attributeLength +"d", newId);
        } catch (SQLException e) {
            throw new RuntimeException("Error generating Employee ID", e);
        }
    }
}
