package org.example.dao.impl;

import org.example.dao.SkillDao;
import org.example.entity.SkillEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SkillDaoImpl implements SkillDao {
    public SkillDaoImpl(Connection db) {
        this.db = db;
    }

    @Override
    public List<SkillEntity> getAll() {
        String sql = sqlQueryGetAll();
        ResultSet result = null;
        PreparedStatement command = null;
        ArrayList<SkillEntity> list = new ArrayList();
        try {
            command = db.prepareStatement(sql);
            result = command.executeQuery();
            while (result.next()) {
                list.add(createSkillEntity(result));
            }

        } catch (SQLException excecao_sql) {
            throw new RuntimeException("Error while running sql", excecao_sql);
        } finally {
            closeResultSetAndStatement(result, command);
        }

        return list;
    }

    private String sqlQueryGetAll() {
        return """
                
                SELECT * FROM skills
                ORDER BY id ASC;""";
    }

    @Override
    public SkillEntity create(SkillEntity skill) {
        SkillEntity newSkill = skill;
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            String sql = sqlCreate();
            command = db.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            command.setString(1, skill.getTitle());
            command.setString(2, skill.getDescription());
            command.executeUpdate();
            ResultSet generatedKeys = command.getGeneratedKeys();
            if (generatedKeys.next()) {
                newSkill.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException excecao_sql) {
            throw new RuntimeException("Error while running sql", excecao_sql);
        } finally {
            closeResultSetAndStatement(result, command);
        }

        return newSkill;
    }

    private String sqlCreate() {
        return "INSERT INTO skills(title, description) VALUES (?,?)";
    }

    @Override
    public List<SkillEntity> insertToPerson(SkillEntity skill, Integer id) {
        String sql = sqlInsertByPerson();
        ResultSet result = null;
        PreparedStatement command = null;
        ArrayList<SkillEntity> list = new ArrayList();
        try {
            command = db.prepareStatement(sql);
            command.setInt(1, skill.getId());
            command.setInt(2, id);
            command.executeUpdate();
        } catch (SQLException excecao_sql) {
            throw new RuntimeException("Error while running sql", excecao_sql);
        } finally {
            closeResultSetAndStatement(result, command);
        }

        return list;
    }

    private String sqlInsertByPerson() {
        return """
                INSERT INTO skills_people (skills_id, people_id) VALUES (?,?)""";
    }

    @Override
    public SkillEntity getById(Integer id) {
        String sql = sqlQueryGetById();
        SkillEntity skill = null;
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            command = db.prepareStatement(sql);
            command.setInt(1, id);
            result = command.executeQuery();
            if (result != null && result.next()) {
                skill = createSkillEntity(result);
            }

        } catch (SQLException excecao_sql) {
            throw new RuntimeException("Error while running sql", excecao_sql);
        } finally {
            closeResultSetAndStatement(result, command);
        }

        return skill;
    }

    private String sqlQueryGetById() {
        return """
                SELECT * FROM skills WHERE skills.id = ? LIMIT 1;""";
    }

    @Override
    public List<SkillEntity> getByIdPerson(Integer id) {
        String sql = sqlQueryGetByIdPerson();
        ResultSet result = null;
        PreparedStatement command = null;
        ArrayList<SkillEntity> list = new ArrayList();
        try {
            command = db.prepareStatement(sql);
            command.setInt(1, id);
            result = command.executeQuery();
            while (result.next()) {
                list.add(createSkillEntity(result));
            }

        } catch (SQLException exception_sql) {
            exception_sql.printStackTrace();
        } finally {
            closeResultSetAndStatement(result, command);
        }

        return list;
    }

    private String sqlQueryGetByIdPerson() {
        return """
                
                SELECT sk.title, sk.description, sk.id FROM skills_people sp
                INNER JOIN skills sk ON sk.id = sp.skills_id
                WHERE sp.people_id = ?;""";
    }

    @Override
    public void updateById(SkillEntity skill) {
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            String sql = sqlUpdate();
            command = db.prepareStatement(sql);
            command.setString(1, skill.getTitle());
            command.setString(2, skill.getDescription());
            command.setInt(3, skill.getId());
            command.executeUpdate();
        } catch (SQLException excecao_sql) {
            throw new RuntimeException("Error while running sql", excecao_sql);
        } finally {
            closeResultSetAndStatement(result, command);
        }

    }

    private String sqlUpdate() {
        return """
                UPDATE skills SET title = ?, description = ? WHERE id = ?;""";
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
            throw new RuntimeException("Error while running sql", excecao_sql);
        } finally {
            closeResultSetAndStatement(result, command);
        }

    }

    private String sqlDelete() {
        return "DELETE FROM skills WHERE id = ?";
    }

    @Override
    public void deleteByIdPerson(Integer idPerson) {
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            String sql = sqlDeleteByIdPerson();
            command = db.prepareStatement(sql);
            command.setInt(1, idPerson);
            command.executeUpdate();
        } catch (SQLException excecao_sql) {
            throw new RuntimeException("Error while running sql", excecao_sql);
        } finally {
            closeResultSetAndStatement(result, command);
        }

    }

    private String sqlDeleteByIdPerson() {
        return "DELETE FROM skills_people WHERE people_id = ?";
    }

    private void closeResultSetAndStatement(ResultSet result, PreparedStatement statement) {
        try {
            if (result != null) {
                result.close();
            }

        } catch (SQLException excecao_sql) {
            throw new RuntimeException("Error while running sql", excecao_sql);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }

            } catch (SQLException excecao_sql) {
                throw new RuntimeException("Error while running sql", excecao_sql);
            }

        }

    }

    private SkillEntity createSkillEntity(ResultSet result) throws SQLException {
        Integer id = result.getInt("id");
        String title = result.getString("title");
        String description = result.getString("description");
        return new SkillEntity(title, description, id);
    }

    private Connection db;
}
