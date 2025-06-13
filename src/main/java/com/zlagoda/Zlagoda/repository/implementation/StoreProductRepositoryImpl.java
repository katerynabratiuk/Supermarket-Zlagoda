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

    private static final String FIND_ALL = "SELECT \n" +
            "    sp.upc,\n" +
            "    sp.id_product,\n" +
            "    base.selling_price AS selling_price,\n" +
            "    sp.selling_price AS new_price,\n" +
            "    sp.products_number,\n" +
            "    sp.promotional_product,\n" +
            "    product_name,\n" +
            "    Category.category_name\n"+
            "FROM store_product sp\n" +
            "JOIN store_product base ON sp.upc_prom = base.upc\n" +
            "JOIN Product ON sp.id_product = Product.id_product\n" +
            "JOIN Category ON Category.category_number = Product.category_number\n" +
            "\n" +
            "UNION\n" +
            "\n" +
            "SELECT \n" +
            "    sp.upc,\n" +
            "    sp.id_product,\n" +
            "    sp.selling_price AS selling_price,\n" +
            "    NULL AS new_price,\n" +
            "    sp.products_number,\n" +
            "    sp.promotional_product,\n" +
            "    product_name,\n" +
            "    Category.category_name\n" +
            "FROM store_product sp\n" +
            "JOIN Product ON sp.id_product = Product.id_product\n" +
            "JOIN Category ON Category.category_number = Product.category_number\n" +
            "WHERE sp.promotional_product = FALSE\n" +
            "  AND sp.upc NOT IN (SELECT upc_prom FROM store_product WHERE upc_prom IS NOT NULL);\n";
    private static final String CREATE = "INSERT INTO Store_product VALUES (?,?,?,?,?,?)";
    private static final String UPDATE = "UPDATE Store_product SET UPC_prom=?, id_product=?, " +
            "selling_price=?, products_number=?, promotional_product=?, promotional_product=? WHERE UPC=?";
    private static final String DELETE = "DELETE FROM Store_product WHERE UPC=?";
    private static final String FIND_BY_ID =
            "SELECT \n" +
                    "    sp.upc,\n" +
                    "    sp.id_product,\n" +
                    "    base.selling_price AS selling_price,\n" +
                    "    sp.selling_price AS new_price,\n" +
                    "    sp.products_number,\n" +
                    "    sp.promotional_product,\n" +
                    "    product_name\n" +
                    "FROM store_product sp " +
                    "JOIN store_product base ON sp.upc_prom = base.upc " +
                    "JOIN product p ON sp.id_product = p.id_product " +
                    "JOIN category c ON p.category_number = c.category_number " +
                    "WHERE sp.upc = ? " +

                    "UNION " +

                    "SELECT " +
                    "    sp.upc, " +
                    "    sp.upc_prom, " +
                    "    sp.id_product, " +
                    "    sp.selling_price AS selling_price, " +
                    "    NULL AS new_price, " +
                    "    sp.products_number, " +
                    "    sp.promotional_product, " +
                    "    p.*, " +
                    "    c.* " +
                    "FROM store_product sp " +
                    "JOIN product p ON sp.id_product = p.id_product " +
                    "JOIN category c ON p.category_number = c.category_number " +
                    "WHERE sp.upc = ? AND (sp.promotional_product = FALSE OR sp.upc_prom IS NULL);";

    private static final String FIND_BY_CATEGORY =
            "SELECT\n"
                    + "sp.upc,\n"
                    + "sp.id_product,\n"
                    + "base.selling_price AS selling_price,\n"
                    + "sp.selling_price AS new_price,\n"
                    + "sp.products_number,\n"
                    + "sp.promotional_product,\n"
                    + "p.product_name,\n"
                    + "p.characteristics,\n"
                    + "c.category_number,\n"
                    + "c.category_name\n"
                    + "FROM store_product sp\n"
                    + "JOIN store_product base ON sp.upc_prom = base.upc\n"
                    + "JOIN product p ON sp.id_product = p.id_product\n"
                    + "JOIN product base_p ON base.id_product = base_p.id_product\n"
                    + "JOIN category c ON base_p.category_number = c.category_number\n"
                    + "WHERE c.category_name = ?\n"
                    + "UNION\n"
                    + "SELECT\n"
                    + "sp.upc,\n"
                    + "sp.id_product,\n"
                    + "sp.selling_price AS selling_price,\n"
                    + "NULL AS new_price,\n"
                    + "sp.products_number,\n"
                    + "sp.promotional_product,\n"
                    + "p.product_name,\n"
                    + "p.characteristics,\n"
                    + "c.category_number,\n"
                    + "c.category_name\n"
                    + "FROM store_product sp\n"
                    + "JOIN product p ON sp.id_product = p.id_product\n"
                    + "JOIN category c ON p.category_number = c.category_number\n"
                    + "WHERE c.category_name = ?\n"
                    + "AND sp.promotional_product = FALSE\n"
                    + "AND sp.upc NOT IN (\n"
                    + "SELECT upc_prom FROM store_product WHERE upc_prom IS NOT NULL\n"
                    + ");\n"
                    + "";
    private static final String FIND_PROMOTIONAL =
            "SELECT\n"
                    + "sp.upc,\n"
                    + "sp.upc_prom,\n"
                    + "sp.id_product,\n"
                    + "base.selling_price AS selling_price,\n"
                    + "sp.selling_price AS new_price,\n"
                    + "sp.products_number,\n"
                    + "sp.promotional_product,\n"
                    + "p.product_name,\n"
                    + "p.characteristics,\n"
                    + "c.category_number,\n"
                    + "c.category_name\n"
                    + "FROM store_product sp\n"
                    + "JOIN store_product base ON sp.upc_prom = base.upc\n"
                    + "JOIN product p ON sp.id_product = p.id_product\n"
                    + "JOIN product base_p ON base.id_product = base_p.id_product\n"
                    + "JOIN category c ON base_p.category_number = c.category_number\n"
                    + "WHERE sp.promotional_product = TRUE\n"
                    + "AND c.category_name = ?\n"
                    + "";
    private static final String FIND_BY_NAME = "SELECT UPC, product_name, selling_price " +
            "FROM Store_Product " +
            "INNER JOIN Product ON Store_Product.id_product = Product.id_product " +
            "WHERE product_name ILIKE ?";
    private static final String FIND_NON_PROMOTIONAL = FIND_ALL + " WHERE promotional_product = FALSE";

    private final DBConnection dbConnection;

    public StoreProductRepositoryImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<StoreProduct> findByCategory(String categoryName) {
        try (Connection connection = dbConnection.getConnection()) {
            ArrayList<StoreProduct> res = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(FIND_BY_CATEGORY);
            stmt.setString(1, categoryName);
            stmt.setString(2, categoryName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                StoreProduct.Builder spBuilder = new StoreProduct.Builder()
                        .setUPC(rs.getString("upc"))
                        .setSellingPrice(rs.getBigDecimal("selling_price"))
                        .setPromotional(rs.getBoolean("promotional_product"))
                        .setProductsNumber(rs.getInt("products_number"));

                BigDecimal newPrice = rs.getBigDecimal("new_price");
                if (newPrice != null) {
                    spBuilder.setNewPrice(newPrice);
                }

                Product product = new Product.Builder()
                        .setName(rs.getString("product_name"))
                        .setId(rs.getInt("id_product"))
                        .build();

                spBuilder.setProduct(product);

                res.add(spBuilder.build());
            }
            return res;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find StoreProduct by UPC", e);
        }
    }

    @Override
    public List<StoreProduct> findAll() {
        try (Connection connection = dbConnection.getConnection()) {
            ArrayList<StoreProduct> res = new ArrayList<>();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(FIND_ALL);

            while (rs.next()) {
                StoreProduct.Builder spBuilder = new StoreProduct.Builder()
                        .setUPC(rs.getString("upc"))
                        .setSellingPrice(rs.getBigDecimal("selling_price"))
                        .setPromotional(rs.getBoolean("promotional_product"))
                        .setProductsNumber(rs.getInt("products_number"));

                BigDecimal newPrice = rs.getBigDecimal("new_price");
                if (newPrice != null) {
                    spBuilder.setNewPrice(newPrice);
                }

                Product product = new Product.Builder()
                        .setName(rs.getString("product_name"))
                        .setId(rs.getInt("id_product"))
                        .setCategory(new Category(rs.getString("category_name")))
                        .build();

                spBuilder.setProduct(product);

                res.add(spBuilder.build());
            }
            return res;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find StoreProduct by UPC", e);
        }
    }


    @Override
    public List<StoreProduct> findByName(String name) {
        try (Connection connection = dbConnection.getConnection()) {
            ArrayList<StoreProduct> res = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(FIND_BY_NAME);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                StoreProduct storeProduct = new StoreProduct.Builder()
                        .setUPC(rs.getString("UPC"))
                        .setProduct(new Product.Builder()
                                .setName(rs.getString("product_name")).build())
                        .setSellingPrice(rs.getBigDecimal("selling_price"))
                        .build();
                res.add(storeProduct);
            }
            return res;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find StoreProduct by UPC", e);
        }
    }

    @Override
    public StoreProduct findById(String UPC) {
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID);
            stmt.setString(1, UPC);
            stmt.setString(2, UPC);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractStoreProduct(rs);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find StoreProduct by UPC", e);
        }
        return null;
    }


    @Override
    public void create(StoreProduct storeProduct) {
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(CREATE);
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
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(UPDATE);
            stmt.setString(1, storeProduct.getUPC());
            stmt.setString(2, storeProduct.getUPC_prom());
            stmt.setInt(3, storeProduct.getProduct().getId());
            stmt.setBigDecimal(4, storeProduct.getSellingPrice());
            stmt.setInt(5, storeProduct.getProductsNumber());
            stmt.setBoolean(6, storeProduct.isPromotional());
            stmt.setString(7, storeProduct.getUPC()); // WHERE clause
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update StoreProduct", e);
        }
    }

    @Override
    public void delete(String UPC) {
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(DELETE);
            stmt.setString(1, UPC);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete StoreProduct", e);
        }
    }


    @Override
    public List<StoreProduct> findPromotional() {
        return findByPromotionStatus(true);
    }

    @Override
    public List<StoreProduct> findNonPromotional() {
        return findByPromotionStatus(false);
    }

    private List<StoreProduct> findByPromotionStatus(boolean isPromotional) {
        String sql = isPromotional ? FIND_PROMOTIONAL : FIND_NON_PROMOTIONAL;

        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            List<StoreProduct> result = new ArrayList<>();

            while (rs.next()) {
                Product product = new Product.Builder()
                        .setName(rs.getString("product_name"))
                        .build();

                StoreProduct storeProduct = new StoreProduct.Builder()
                        .setUPC(rs.getString("UPC"))
                        .setSellingPrice(rs.getBigDecimal("selling_price"))
                        .setProduct(product)
                        .build();

                result.add(storeProduct);
            }

            return result;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find promotional store products", e);
        }
    }



    private StoreProduct extractStoreProduct(ResultSet rs) throws SQLException {
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
                .setUPC(rs.getString("UPC"))
                .setUPC_prom(rs.getString("UPC_prom"))
                .setProduct(product)
                .setSellingPrice(rs.getBigDecimal("selling_price"))
                .setNewPrice(rs.getBigDecimal("new_price"))
                .setProductsNumber(rs.getInt("products_number"))
                .setPromotional(rs.getBoolean("promotional_product"))
                .build();
    }


}
