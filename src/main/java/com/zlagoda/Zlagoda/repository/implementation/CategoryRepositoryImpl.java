package com.zlagoda.Zlagoda.repository.implementation;

import com.zlagoda.Zlagoda.entity.Category;
import com.zlagoda.Zlagoda.exception.DataAccessException;
import com.zlagoda.Zlagoda.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private static final String GET_ALL = "SELECT * FROM Category";
    private static final String CREATE = "INSERT INTO Category(category_name) VALUES (?)";
    private static final String UPDATE = "UPDATE Category SET category_name=? WHERE category_number=?";
    private static final String DELETE = "DELETE FROM Category WHERE category_number=?";
    private static final String FIND_BY_ID = "SELECT * FROM Category WHERE category_number=?";
    private static final String FIND_BY_NAME = "SELECT * FROM Category WHERE category_name ILIKE ?";


    private final DBConnection dbConnection;

    public CategoryRepositoryImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Category> findAll() {
        List<Category> res = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(GET_ALL);

            while(results.next())
            {
                Category category = extractCategoryFromResultSet(results);
                res.add(category);
            }
            return res;
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Failed to find all categories", e);
        }
    }

    @Override
    public List<Category> filter() {
        List<Category> res = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(GET_ALL + " ORDER BY category_name");

            while(results.next())
            {
                Category category = extractCategoryFromResultSet(results);
                res.add(category);
            }
            return res;
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Failed to find all categories", e);
        }
    }

    @Override
    public Category findById(Integer id) {
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID);
            stmt.setInt(1, id);
            ResultSet results = stmt.executeQuery();

            if (results.next()) {
                return extractCategoryFromResultSet(results);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find the category", e);
        }
        return null;
    }

    @Override
    public void create(Category category) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(CREATE)) {

            stmt.setString(1, category.getName());

            stmt.execute();

        } catch (SQLException e) {
            throw new DataAccessException("Failed to create a category", e);
        }
    }

    @Override
    public void update(Category category) {
        try(Connection connection = dbConnection.getConnection())
        {
            PreparedStatement stmt = connection.prepareStatement(UPDATE);

            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getNumber());
            stmt.executeUpdate();

        } catch (SQLException e)
        {
            throw new DataAccessException("Failed to update the category", e);
        }

    }

    @Override
    public void delete(Integer id) {
        try(Connection connection = dbConnection.getConnection())
        {
            PreparedStatement stmt = connection.prepareStatement(DELETE);

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e)
        {
            throw new DataAccessException("Failed to delete the category", e);
        }
    }

    @Override
    public List<Category> findByName(String name) {
        List<Category> res = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(FIND_BY_NAME);
            stmt.setString(1, name + '%');
            ResultSet results = stmt.executeQuery();

            while(results.next())
            {
                Category category = extractCategoryFromResultSet(results);
                res.add(category);
            }
            return res;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find the category", e);
        }
    }

    private Category extractCategoryFromResultSet(ResultSet results) throws SQLException {
        return new Category (
                results.getInt("category_number"),
                results.getString("category_name")
        );
    }
}
