package com.zlagoda.Zlagoda.repository.implementation;

import com.zlagoda.Zlagoda.entity.Category;
import com.zlagoda.Zlagoda.entity.Product;
import com.zlagoda.Zlagoda.entity.StoreProduct;
import com.zlagoda.Zlagoda.exception.DataAccessException;
import com.zlagoda.Zlagoda.repository.StoreProductRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StoreProductRepositoryImpl implements StoreProductRepository {

    private static final String FIND_ALL_WITH_PROMOTION_PRIORITY =
            "SELECT DISTINCT ON (pr.id_product) " +
                    "sp.upc, sp.upc_prom, sp.id_product, " +
                    "COALESCE(base.selling_price, sp.selling_price) AS selling_price, " +
                    "CASE WHEN sp.promotional_product THEN sp.selling_price ELSE NULL END AS new_price, " +
                    "sp.products_number, sp.promotional_product, " +
                    "pr.product_name, pr.characteristics, " +
                    "c.category_number, c.category_name " +
                    "FROM store_product sp " +
                    "JOIN product pr ON sp.id_product = pr.id_product " +
                    "JOIN category c ON pr.category_number = c.category_number " +
                    "LEFT JOIN store_product base ON sp.upc_prom = base.upc " +
                    "ORDER BY pr.id_product, sp.promotional_product DESC";

    private static final String FIND_BY_CATEGORY =
            "SELECT DISTINCT ON (pr.id_product) " +
                    "sp.upc, sp.upc_prom, sp.id_product, sp.selling_price, sp.products_number, sp.promotional_product, " +
                    "pr.product_name, pr.characteristics, " +
                    "c.category_number, c.category_name, " +
                    "base.selling_price AS new_price " +
                    "FROM store_product sp " +
                    "JOIN product pr ON sp.id_product = pr.id_product " +
                    "JOIN category c ON pr.category_number = c.category_number " +
                    "LEFT JOIN store_product base ON sp.upc_prom = base.upc " +
                    "WHERE c.category_name = ? " +
                    "ORDER BY pr.id_product, sp.promotional_product DESC;";

    private static final String FIND_BY_ID =
            "SELECT sp.*, pr.*, c.*, base.selling_price AS new_price " +
                    "FROM store_product sp " +
                    "JOIN product pr ON sp.id_product = pr.id_product " +
                    "JOIN category c ON pr.category_number = c.category_number " +
                    "LEFT JOIN store_product base ON sp.upc_prom = base.upc " +
                    "WHERE sp.upc = ?";

    private static final String FILTER_QUERY =
            "SELECT DISTINCT ON (pr.id_product) " +
                    "sp.upc, sp.upc_prom, sp.id_product, " +
                    "COALESCE(base.selling_price, sp.selling_price) AS selling_price, " +
                    "CASE WHEN sp.promotional_product THEN sp.selling_price ELSE NULL END AS new_price, " +
                    "sp.products_number, sp.promotional_product, " +
                    "pr.product_name, pr.characteristics, " +
                    "c.category_number, c.category_name " +
                    "FROM store_product sp " +
                    "JOIN product pr ON sp.id_product = pr.id_product " +
                    "JOIN category c ON pr.category_number = c.category_number " +
                    "LEFT JOIN store_product base ON sp.upc_prom = base.upc ";


    private static final String FIND_BY_NAME =
            "SELECT sp.upc, pr.product_name, sp.selling_price " +
                    "FROM store_product sp " +
                    "JOIN product pr ON sp.id_product = pr.id_product " +
                    "WHERE pr.product_name ILIKE ?";

    private static final String CREATE = "INSERT INTO store_product VALUES (?,?,?,?,?,?)";

    private static final String UPDATE = "UPDATE store_product SET upc_prom=?, id_product=?, " +
            "selling_price=?, products_number=?, promotional_product=? WHERE upc=?";

    private static final String DELETE = "DELETE FROM store_product WHERE upc=?";

    private final DBConnection dbConnection;

    public StoreProductRepositoryImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    private StoreProduct extractStoreProductProm(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setNumber(rs.getInt("category_number"));
        category.setName(rs.getString("category_name"));

        Product product = new Product.Builder()
                .setId(rs.getInt("id_product"))
                .setName(rs.getString("product_name"))
                .setCharacteristics(rs.getString("characteristics"))
                .setCategory(category)
                .build();

        return new StoreProduct.Builder()
                .setUPC(rs.getString("upc"))
                .setUPC_prom(rs.getString("upc_prom"))
                .setProduct(product)
                .setSellingPrice(rs.getBigDecimal("selling_price"))
                .setNewPrice(rs.getBigDecimal("new_price"))
                .setProductsNumber(rs.getInt("products_number"))
                .setPromotional(rs.getBoolean("promotional_product"))
                .build();
    }

    @Override
    public List<StoreProduct> findAll() {
        try (Connection connection = dbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(FIND_ALL_WITH_PROMOTION_PRIORITY)) {

            List<StoreProduct> res = new ArrayList<>();
            while (rs.next()) {
                res.add(extractStoreProductProm(rs));
            }
            return res;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find all StoreProducts", e);
        }
    }

    @Override
    public List<StoreProduct> findByCategory(String categoryName) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_CATEGORY)) {

            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();

            List<StoreProduct> res = new ArrayList<>();
            while (rs.next()) {
                res.add(extractStoreProductProm(rs));
            }
            return res;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find StoreProduct by category", e);
        }
    }

    @Override
    public StoreProduct findById(String upc) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID)) {

            stmt.setString(1, upc);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractStoreProductProm(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find StoreProduct by UPC", e);
        }
    }

    @Override
    public List<StoreProduct> findByName(String name) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_NAME)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            List<StoreProduct> res = new ArrayList<>();
            while (rs.next()) {
                StoreProduct sp = new StoreProduct.Builder()
                        .setUPC(rs.getString("upc"))
                        .setProduct(new Product.Builder().setName(rs.getString("product_name")).build())
                        .setSellingPrice(rs.getBigDecimal("selling_price"))
                        .build();
                res.add(sp);
            }
            return res;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find StoreProduct by name", e);
        }
    }

    @Override
    public void create(StoreProduct storeProduct) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(CREATE)) {

            stmt.setString(1, storeProduct.getUPC());
            stmt.setString(2, storeProduct.getUPC_prom());
            stmt.setInt(3, storeProduct.getProduct().getId());
            stmt.setBigDecimal(4, storeProduct.getSellingPrice());
            stmt.setInt(5, storeProduct.getProductsNumber());
            stmt.setBoolean(6, storeProduct.isPromotional());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create StoreProduct", e);
        }
    }

    @Override
    public void update(StoreProduct storeProduct) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE)) {

            stmt.setString(1, storeProduct.getUPC_prom());
            stmt.setInt(2, storeProduct.getProduct().getId());
            stmt.setBigDecimal(3, storeProduct.getSellingPrice());
            stmt.setInt(4, storeProduct.getProductsNumber());
            stmt.setBoolean(5, storeProduct.isPromotional());
            stmt.setString(6, storeProduct.getUPC());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update StoreProduct", e);
        }
    }

    @Override
    public void delete(String upc) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(DELETE)) {

            stmt.setString(1, upc);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete StoreProduct", e);
        }
    }

    @Override
    public List<StoreProduct> filter(Boolean promotional, String category, List<String> sortBy) {
        StringBuilder innerQuery = new StringBuilder(FILTER_QUERY );

        List<Object> parameters = new ArrayList<>();
        boolean hasWhere = false;

        if (promotional != null) {
            innerQuery.append("WHERE sp.promotional_product = ? ");
            parameters.add(promotional);
            hasWhere = true;
            if (!promotional) {
                innerQuery.append("AND sp.upc NOT IN (SELECT upc_prom FROM store_product WHERE upc_prom IS NOT NULL) ");
            }
        }

        if (category != null && !category.isBlank()) {
            if (!hasWhere) {
                innerQuery.append("WHERE ");
            } else {
                innerQuery.append("AND ");
            }
            innerQuery.append("c.category_name = ? ");
            parameters.add(category);
        }

        innerQuery.append("ORDER BY pr.id_product, sp.promotional_product DESC");

        StringBuilder finalQuery = new StringBuilder("SELECT * FROM (");
        finalQuery.append(innerQuery);
        finalQuery.append(") AS filtered_products ");

        // outer sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            finalQuery.append("ORDER BY ");
            for (int i = 0; i < sortBy.size(); i++) {
                if (i > 0) finalQuery.append(", ");
                switch (sortBy.get(i)) {
                    case "product.product_name":
                        finalQuery.append("product_name");
                        break;
                    case "quantity":
                        finalQuery.append("products_number");
                        break;
                    default:
                        break;
                }
            }
        }

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(finalQuery.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            List<StoreProduct> result = new ArrayList<>();
            while (rs.next()) {
                result.add(extractStoreProductProm(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to filter StoreProduct", e);
        }
    }

    @Override
    public void addPromotional(StoreProduct promoProduct) {
        promoProduct.setPromotional(true);
        String nonPromoUPC = promoProduct.getUPC();
        String promoUPC = promoProduct.getUPC_prom();

        promoProduct.setUPC_prom(nonPromoUPC);
        promoProduct.setUPC(promoUPC);

        promoProduct.setSellingPrice(
                promoProduct.getSellingPrice().multiply(BigDecimal.valueOf(0.8))
        );

        this.create(promoProduct);

    }


}
