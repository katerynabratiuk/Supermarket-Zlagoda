package com.zlagoda.Zlagoda.repository.implementation;

import com.zlagoda.Zlagoda.entity.Employee;
import com.zlagoda.Zlagoda.exception.DataAccessException;
import com.zlagoda.Zlagoda.repository.EmployeeRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private static final String GET_ALL = "SELECT * FROM Employee";
    private static final String CREATE = "INSERT INTO Employee VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE = "UPDATE Employee SET empl_surname=?, empl_name=?, empl_patronymic=?, empl_role=?, salary=?, " +
            "date_of_birth=?, date_of_start=?, phone_number=?, city=?, street=?, zip_code=? " +
            "WHERE id_employee=?";
    private static final String DELETE = "DELETE FROM Employee WHERE id_employee=?";

    private static final String FIND_BY_ID = "SELECT * FROM Employee WHERE id_employee=?";
    private static final String FIND_BY_SURNAME = "SELECT * FROM Employee WHERE empl_surname ILIKE ?";
    private static final String FIND_BY_ROLE = "SELECT * FROM Employee WHERE empl_role=?";

    private final DBConnection dbConnection;

    public EmployeeRepositoryImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Employee> findByRole(String role) {
        List<Employee> res = new ArrayList<>();

        try(Connection connection = dbConnection.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ROLE);
            statement.setString(1, role);
            ResultSet results = statement.executeQuery();

            while(results.next())
            {
                Employee empl = extractEmployeeFromResultSet(results);
                res.add(empl);
            }

        }
        catch (SQLException e){
            throw new DataAccessException("Failed to find employees", e);
        }
        return res;
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> res = new ArrayList<>();

        try(Connection connection = dbConnection.getConnection())
        {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(GET_ALL);

            while(results.next())
            {
                Employee empl = extractEmployeeFromResultSet(results);
                res.add(empl);
            }

        }
        catch (SQLException e){
            throw new DataAccessException("Failed to find employees", e);
        }
        return res;
    }

    @Override
    public List<Employee> findByName(String name) {
        List<Employee> res = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(FIND_BY_SURNAME);
            stmt.setString(1, name + '%');
            ResultSet results = stmt.executeQuery();

            while(results.next())
            {
                Employee emp = extractEmployeeFromResultSet(results);
                res.add(emp);
            }
            return res;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find employees", e);
        }
    }

    @Override
    public Employee findById(String id) {
        Employee employee = new Employee();
        try (Connection connection = dbConnection.getConnection())
        {

            PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                employee.setId(rs.getString("id_employee"));
                employee.setName(rs.getString("empl_name"));
                employee.setSurname(rs.getString("empl_surname"));
                employee.setPatronymic(rs.getString("empl_patronymic"));
                employee.setRole(rs.getString("empl_role"));
                employee.setSalary(rs.getBigDecimal("salary"));
                employee.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                employee.setDateOfStart(rs.getDate("date_of_start").toLocalDate());
                employee.setPhoneNumber(rs.getString("phone_number"));
                employee.setCity(rs.getString("city"));
                employee.setStreet(rs.getString("street"));
                employee.setZipCode(rs.getString("zip_code"));
            }
            return employee;
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Failed to find employees", e);
        }
    }

    @Override
    public void create(Employee employee) {
        try(Connection connection = dbConnection.getConnection())
        {
            PreparedStatement stmt = connection.prepareStatement(CREATE);
            stmt.setString(1, employee.getId());
            stmt.setString(2, employee.getSurname());
            stmt.setString(3, employee.getName());
            stmt.setString(4, employee.getPatronymic());
            stmt.setString(5, employee.getRole());
            stmt.setBigDecimal(6, employee.getSalary());
            stmt.setDate(7, Date.valueOf(employee.getDateOfBirth()));
            stmt.setDate(8, Date.valueOf(employee.getDateOfStart()));
            stmt.setString(9, employee.getPhoneNumber());
            stmt.setString(10, employee.getCity());
            stmt.setString(11, employee.getStreet());
            stmt.setString(12, employee.getZipCode());

            stmt.executeUpdate();
        }
        catch (SQLException e){
            throw new DataAccessException("Failed to create employees", e);
        }
    }

    @Override
    public void update(Employee employee) {
        try (Connection connection = dbConnection.getConnection())
        {
            PreparedStatement stmt = connection.prepareStatement(UPDATE);
            stmt.setString(1, employee.getSurname());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getPatronymic());
            stmt.setString(4, employee.getRole());
            stmt.setBigDecimal(5, employee.getSalary());
            stmt.setDate(6, Date.valueOf(employee.getDateOfBirth()));
            stmt.setDate(7, Date.valueOf(employee.getDateOfStart()));
            stmt.setString(8, employee.getPhoneNumber());
            stmt.setString(9, employee.getCity());
            stmt.setString(10, employee.getStreet());
            stmt.setString(11, employee.getZipCode());
            stmt.setString(12, employee.getId());
            stmt.executeUpdate();
        } catch (SQLException e){
            throw new DataAccessException("Failed to update employees", e);
        }
    }

    @Override
    public void delete(String id) {
        try (Connection connection = dbConnection.getConnection())
        {
            PreparedStatement stmt = connection.prepareStatement(DELETE);
            stmt.setString(1,id);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Failed to delete employee", e);
        }
    }

    private static Employee extractEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        return new Employee(
                resultSet.getString("id_employee"),
                resultSet.getString("empl_name"),
                resultSet.getString("empl_surname"),
                resultSet.getString("empl_patronymic"),
                resultSet.getString("empl_role"),
                resultSet.getBigDecimal("salary"),
                resultSet.getDate("date_of_birth").toLocalDate(),
                resultSet.getDate("date_of_start").toLocalDate(),
                resultSet.getString("phone_number"),
                resultSet.getString("city"),
                resultSet.getString("street"),
                resultSet.getString("zip_code"),
                resultSet.getString("empl_username")
        );
    }
}
