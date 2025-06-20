package com.zlagoda.Zlagoda.repository.implementation;

import com.zlagoda.Zlagoda.entity.Category;
import com.zlagoda.Zlagoda.entity.Product;
import com.zlagoda.Zlagoda.entity.StoreProduct;
import com.zlagoda.Zlagoda.exception.DataAccessException;
import com.zlagoda.Zlagoda.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final String CREATE = "INSERT INTO Product (category_number, product_name, characteristics) VALUES (?, ?, ?)";
    private final String FIND_BY_NAME = "SELECT Product.* FROM Product WHERE product_name ILIKE ?";
    private final String UPDATE = "UPDATE Product SET product_name=?, characteristics=?, category_number=? WHERE id_product=?";

    private final DBConnection dbConnection;

    public ProductRepositoryImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }


    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public List<Product> findByName(String name) {
        try (Connection connection = dbConnection.getConnection()) {
            List<Product> res = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(FIND_BY_NAME);
            stmt.setString(1, "%" + name + "%"); // для ILIKE
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product.Builder()
                        .setId(rs.getInt("id_product"))
                        .setName(rs.getString("product_name"))
                        .setCharacteristics(rs.getString("characteristics"))
                        .setCategory(new Category.Builder()
                                .setId(rs.getInt("category_number"))
                                .build())
                        .build();
                res.add(product);
            }
            return res;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find product by name", e);
        }
    }

    @Override
    public Product findById(Integer integer) {
        return null;
    }

    @Override
    public void create(Product product) {
        try(Connection connection = dbConnection.getConnection())
        {
            PreparedStatement stmt = connection.prepareStatement(CREATE);
            stmt.setInt(1, product.getCategory().getNumber());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCharacteristics());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            throw new DataAccessException("Failed to create product", e);
        }
    }

    @Override
    public void update(Product p) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE)) {

            stmt.setString(1, p.getName());
            stmt.setString(2, p.getCharacteristics());
            stmt.setInt(3, p.getCategory().getNumber());
            stmt.setInt(4, p.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update StoreProduct", e);
        }
    }

    @Override
    public void delete(Integer integer) {

    }
}
