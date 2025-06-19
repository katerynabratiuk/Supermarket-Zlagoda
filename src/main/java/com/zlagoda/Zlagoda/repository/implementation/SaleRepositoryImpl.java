package com.zlagoda.Zlagoda.repository.implementation;

import com.zlagoda.Zlagoda.entity.Product;
import com.zlagoda.Zlagoda.entity.Receipt;
import com.zlagoda.Zlagoda.entity.Sale;
import com.zlagoda.Zlagoda.entity.StoreProduct;
import com.zlagoda.Zlagoda.exception.DataAccessException;
import com.zlagoda.Zlagoda.repository.SaleRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SaleRepositoryImpl implements SaleRepository {

    private static final String FIND_BY_CHECK_ID =
            "SELECT " +
                    "  Sale.selling_price AS sale_price, " +
                    "  Sale.product_number, " +
                    "  Store_Product.selling_price AS store_price, " +
                    "  Store_Product.UPC, " +
                    "  Product.product_name " +
                    "FROM Sale " +
                    "JOIN Store_Product ON Sale.UPC = Store_Product.UPC " +
                    "JOIN Product ON Store_Product.id_product = Product.id_product " +
                    "WHERE Sale.check_number = ?";


    private final DBConnection dbConnection;

    public SaleRepositoryImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }


    @Override
    public List<Sale> findByCheckId(String checkId) {
        List<Sale> res = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection()){
            PreparedStatement stmt = connection.prepareStatement(FIND_BY_CHECK_ID);
            stmt.setString(1, checkId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    res.add(extractSaleFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find Sales by check ID", e);
        }

        return res;
    }


    private Sale extractSaleFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setName(rs.getString("product_name"));

        StoreProduct sp = new StoreProduct();
        sp.setProduct(product);
        sp.setSellingPrice(rs.getBigDecimal("store_price"));
        sp.setUPC(rs.getString("UPC"));

        Sale sale = new Sale();
        sale.setProductNum(rs.getInt("product_number"));
        sale.setSelling_price(rs.getBigDecimal("sale_price").multiply(BigDecimal.valueOf(sale.getProductNum())));
        sale.setStoreProduct(sp);

        return sale;
    }



    @Override
    public void create(Sale sale) {

    }


}
