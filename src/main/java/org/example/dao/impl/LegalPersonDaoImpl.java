package org.example.dao.impl;

import org.example.dao.LegalPersonDao;
import org.example.dto.LoginDto;
import org.example.entity.AddressEntity;
import org.example.entity.LegalPersonEntity;
import org.example.entity.NaturalPersonEntity;
import org.example.entity.SkillEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LegalPersonDaoImpl implements LegalPersonDao {
    private static final Logger LOGGER = Logger.getLogger(LegalPersonDaoImpl.class.getName());
    private final Connection db;

    public LegalPersonDaoImpl(Connection db) {
        this.db = db;
    }

    @Override
    public List<LegalPersonEntity> getAll() {
        String sql = sqlQueryGetAll();
        List<LegalPersonEntity> list = new ArrayList<>();

        try (PreparedStatement command = db.prepareStatement(sql);
             ResultSet result = command.executeQuery()) {

            while (result.next()) {
                list.add(createLegalPersonEntity(result));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all legal persons", e);
            return Collections.emptyList();
        }

        return list;
    }

    private String sqlQueryGetAll() {
        return """
                SELECT lp.cnpj, p.email, p.description, p.password, p.name, p.id,
                ad.country, ad.state, ad.cep, p.address
                FROM legalpeople lp
                INNER JOIN people p ON lp."idPerson" = p.id
                INNER JOIN address ad ON ad.id = p.address""";
    }

    @Override
    public LegalPersonEntity create(LegalPersonEntity person) {
        String sql = sqlCreate();
        if (person.getCnpj() == null) {
            return null;
        }
        try (PreparedStatement command = db.prepareStatement(sql)) {
            command.setInt(1, person.getId());
            command.setString(2, person.getCnpj());
            command.executeUpdate();
            return person;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating legal person", e);
            return null;
        }
    }

    private String sqlCreate() {
        return "INSERT INTO legalpeople (\"idPerson\", cnpj) VALUES (?, ?);";
    }

    @Override
    public LegalPersonEntity getById(Integer id) {
        String sql = sqlQueryGetById();

        try (PreparedStatement command = db.prepareStatement(sql)) {
            command.setInt(1, id);

            try (ResultSet result = command.executeQuery()) {
                if (result.next()) {
                    return createLegalPersonEntity(result);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving legal person by ID", e);
            throw new RuntimeException("Error while running sql", e);
        }

        return null;
    }

    private String sqlQueryGetById() {
        return """
                SELECT lp.cnpj, p.email, p.description, p.password, p.name, p.id, p.address,
                ad.country, ad.state, ad.cep
                FROM legalpeople lp
                INNER JOIN people p ON lp."idPerson" = p.id
                INNER JOIN address ad ON ad.id = p.address 
                WHERE p.id = ? LIMIT 1""";
    }

    @Override
    public void updateById(LegalPersonEntity person) {
        String sql = sqlUpdate();

        try (PreparedStatement command = db.prepareStatement(sql)) {
            command.setString(1, person.getCnpj());
            command.setInt(2, person.getId());
            command.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating legal person", e);
            throw new RuntimeException("Error while running sql", e);
        }
    }

    private String sqlUpdate() {
        return "UPDATE legalpeople SET cnpj = ? WHERE \"idPerson\" = ?;";
    }

    @Override
    public void deleteById(Integer id) {
        String sql = sqlDelete();

        try (PreparedStatement command = db.prepareStatement(sql)) {
            command.setInt(1, id);
            command.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting legal person", e);
            throw new RuntimeException("Error while running sql", e);
        }
    }

    private String sqlDelete() {
        return "DELETE FROM legalpeople WHERE idPerson = ?;";
    }

    @Override
    public LegalPersonEntity loginPerson(LoginDto req) {
        String sql = sqlQueryGetByEmailAndPassword();

        try (PreparedStatement command = db.prepareStatement(sql)) {
            command.setString(1, req.getEmail());
            command.setString(2, req.getSenha());

            try (ResultSet result = command.executeQuery()) {
                if (result.next()) {
                    return createLegalPersonEntity(result);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error logging in legal person", e);
            throw new RuntimeException("Error while running sql", e);
        }

        return null;
    }

    private String sqlQueryGetByEmailAndPassword() {
        return """
                SELECT lp.cnpj, p.email, p.description, p.password, p.name, p.id, p.address,
                ad.country, ad.state, ad.cep
                FROM legalpeople lp
                INNER JOIN people p ON lp."idPerson" = p.id
                INNER JOIN address ad ON ad.id = p.address 
                WHERE p.email = ? AND p.password = ? LIMIT 1""";
    }

    private LegalPersonEntity createLegalPersonEntity(ResultSet result) throws SQLException {
        return new LegalPersonEntity(
                result.getString("name"),
                result.getString("email"),
                result.getString("password"),
                result.getString("description"),
                new AddressEntity(
                        result.getString("country"),
                        result.getString("state"),
                        result.getString("cep"),
                        result.getInt("address")
                ),
                result.getString("cnpj"),
                result.getInt("id"),
                new ArrayList<>()
        );
    }
}