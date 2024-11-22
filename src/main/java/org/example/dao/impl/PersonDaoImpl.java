package org.example.dao.impl;

import org.example.dao.PersonDao;
import org.example.entity.AddressEntity;
import org.example.entity.NaturalPersonEntity;
import org.example.entity.PersonEntity;

import java.sql.*;

public class PersonDaoImpl implements PersonDao {
    public PersonDaoImpl(Connection db) {
        this.db = db;
    }

    @Override
    public PersonEntity create(PersonEntity person) {
        PersonEntity newPerson = person;
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            String sql = sqlCreate();
            command = db.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            command.setString(1, newPerson.getEmail());
            command.setString(2, newPerson.getName());
            command.setString(3, newPerson.getDescription());
            command.setInt(4, newPerson.getAddress().getId());
            command.setString(5, newPerson.getPassword());
            command.executeUpdate();
            ResultSet generatedKeys = command.getGeneratedKeys();
            if (generatedKeys.next()) {
                newPerson.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException excecao_sql) {
            throw new RuntimeException("Error while running sql", excecao_sql);
        } finally {
            closeResultSetAndStatement(result, command);
        }


        return newPerson;
    }

    private String sqlCreate() {
        return "INSERT INTO people (email, name, description, address, password) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    public PersonEntity getById(Integer id) {
        String sql = sqlQueryGetById();
        PersonEntity person = null;
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            command = db.prepareStatement(sql);
            command.setInt(1, id);
            result = command.executeQuery();
            if (result != null && result.next()) {
                person = createPersonEntity(result);
            }

        } catch (SQLException excecao_sql) {
            excecao_sql.printStackTrace();
        } finally {
            closeResultSetAndStatement(result, command);
        }

        return person;
    }

    private String sqlQueryGetById() {
        return """
                
                SELECT p.email,p.description,p.password,p.name,p.id,p.address,
                ad.country, ad.state, ad.cep\s
                FROM people p\s
                INNER JOIN address ad\s
                ON ad.id = p.address WHERE p.id= ? LIMIT 1;""";
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
            excecao_sql.printStackTrace();
        } finally {
            closeResultSetAndStatement(result, command);
        }

    }

    private String sqlDelete() {
        return """
                DELETE FROM people WHERE id = ?;""";
    }

    @Override
    public void updateById(PersonEntity person) {
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            String sql = sqlUpdate();
            command = db.prepareStatement(sql);
            command.setString(1, person.getName());
            command.setString(2, person.getDescription());
            command.setString(3, person.getEmail());
            command.setString(4, person.getPassword());
            command.setInt(5, person.getId());
            command.executeUpdate();
        } catch (SQLException excecao_sql) {
            excecao_sql.printStackTrace();
        } finally {
            closeResultSetAndStatement(result, command);
        }

    }

    private String sqlUpdate() {
        return """
                
                UPDATE people\s
                SET name = ?, description = ?, email = ?, password = ?
                WHERE id = ?;""";
    }

    private void closeResultSetAndStatement(ResultSet result, PreparedStatement statement) {
        try {
            if (result != null) {
                result.close();
            }

        } catch (SQLException exceção_sql) {
            exceção_sql.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }

            } catch (SQLException exceção_sql) {
                exceção_sql.printStackTrace();
            }

        }

    }
    private PersonEntity createPersonEntity(ResultSet result) throws SQLException {
        Integer id = result.getInt("id");
        String country = result.getString("country");
        String state = result.getString("state");
        String cep = result.getString("cep");
        String email = result.getString("email");
        String name = result.getString("name");
        String password = result.getString("password");
        String description = result.getString("description");
        Integer idAddress = result.getInt("address");
        return new PersonEntity(name, email,password,description,new AddressEntity(country, state, cep, idAddress),id);
    }
    private Connection db;
}
