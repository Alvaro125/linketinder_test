package org.example.dao.impl;

import org.example.dao.AddressDao;
import org.example.entity.AddressEntity;
import org.example.entity.JobEntity;

import java.sql.*;

public class AddressDaoImpl implements AddressDao {
    public AddressDaoImpl(Connection db) {
        this.db = db;
    }

    @Override
    public AddressEntity create(AddressEntity address) {
        AddressEntity newAddress = address;
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            String sql = sqlCreate();
            command = db.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            command.setString(1, address.getCountry());
            command.setString(2, address.getState());
            command.setString(3, address.getCep());
            command.executeUpdate();
            ResultSet generatedKeys = command.getGeneratedKeys();
            if (generatedKeys.next()) {
                newAddress.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while creating address", e);
        } finally {
            closeResultSetAndStatement(result, command);
        }


        return newAddress;
    }

    private String sqlCreate() {
        return "INSERT INTO address (country, state, cep) VALUES (?, ?, ?)";
    }

    @Override
    public AddressEntity getById(Integer id) {
        AddressEntity address = null;
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            command = db.prepareStatement(sqlQueryGetById());
            command.setInt(1, id);
            result = command.executeQuery();
            if (result != null && result.next()) {
                address = createAddressEntity(result);
            }

        } catch (SQLException excecao_sql) {
            excecao_sql.printStackTrace();
        } finally {
            closeResultSetAndStatement(result, command);
        }

        return address;
    }

    private String sqlQueryGetById() {
        return """
                SELECT * FROM address ad WHERE ad.id = ?;\s""";
    }

    @Override
    public void updateById(AddressEntity address) {
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            String sql = sqlUpdate();
            command = db.prepareStatement(sql);
            command.setString(1, address.getCountry());
            command.setString(2, address.getState());
            command.setString(3, address.getCep());
            command.setInt(4, address.getId());
            command.executeUpdate();
        } catch (SQLException excecao_sql) {
            throw new RuntimeException("Error while updating address", excecao_sql);
        } finally {
            closeResultSetAndStatement(result, command);
        }

    }

    private String sqlUpdate() {
        return """
                
                UPDATE address\s
                SET country = ?, state = ?, cep = ?\s
                WHERE id = ?;""";
    }

    @Override
    public void deleteById(Integer id) {
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            String sql = sqlDelete();
            command = db.prepareStatement(sql);
            command.setInt(1, id);
            command.executeUpdate();
        } catch (SQLException excecao_sql) {
            throw new RuntimeException("Error while deleting address", excecao_sql);
        } finally {
            closeResultSetAndStatement(result, command);
        }

    }

    private String sqlDelete() {
        return """
                DELETE FROM address WHERE id = ?;""";
    }

    private void closeResultSetAndStatement(ResultSet result, PreparedStatement statement) {
        try {
            if (result != null) {
                result.close();
            }

            if (statement != null) {
                statement.close();
            }

        } catch (SQLException excecao_sql) {
            throw new RuntimeException("Error while running address", excecao_sql);
        }

    }

    private AddressEntity createAddressEntity(ResultSet result) throws SQLException {
        Integer id = result.getInt("id");
        String country = result.getString("country");
        String state = result.getString("state");
        String cep = result.getString("cep");
        return new AddressEntity(country, state, cep, id);
    }
    private Connection db;
}
