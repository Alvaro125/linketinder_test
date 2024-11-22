package org.example.dao.impl;

import org.example.dao.NaturalPersonDao;
import org.example.dto.LoginDto;
import org.example.entity.AddressEntity;
import org.example.entity.NaturalPersonEntity;
import org.example.entity.SkillEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NaturalPersonDaoImpl implements NaturalPersonDao {
    public NaturalPersonDaoImpl(Connection db) {
        this.db = db;
    }

    @Override
    public List<NaturalPersonEntity> getAll() {
        String sql = sqlQueryGetAll();
        ResultSet result = null;
        PreparedStatement command = null;
        ArrayList<NaturalPersonEntity> list = new ArrayList();
        try {
            command = db.prepareStatement(sql);
            result = command.executeQuery();
            while (result.next()) {
                list.add(createNaturalPersonEntity(result));
            }

        } catch (SQLException excecao_sql) {
            excecao_sql.printStackTrace();
        } finally {
            closeResultSetAndStatement(result, command);
        }

        return list;
    }

    private String sqlQueryGetAll() {
        return ((String) ("SELECT np.cpf,np.age,p.email,p.description,p.password,p.name,p.id, " + "ad.country, ad.state, ad.cep, p.address FROM naturalpeople np " + "INNER JOIN people p ON np.\"idPerson\" = p.id " + "INNER JOIN address ad " + "ON ad.id = p.address;"));
    }

    @Override
    public NaturalPersonEntity getById(Integer id) {
        String sql = sqlQueryGetById();
        NaturalPersonEntity person = null;
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            command = db.prepareStatement(sql);
            command.setInt(1, id);
            result = command.executeQuery();
            if (result != null && result.next()) {
                person = createNaturalPersonEntity(result);
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
                
                SELECT np.cpf, np.age,p.email,p.description,p.password,p.name,p.id,p.address,
                ad.country, ad.state, ad.cep\s
                FROM naturalpeople np\s
                INNER JOIN people p\s
                ON np."idPerson" = p.id\s
                INNER JOIN address ad\s
                ON ad.id = p.address WHERE p.id= ? LIMIT 1;""";
    }

    @Override
    public void updateById(NaturalPersonEntity person) {
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            String sql = sqlUpdate();
            command = db.prepareStatement(sql);
            command.setString(1, person.getCpf());
            command.setInt(2, person.getAge());
            command.setInt(3, person.getId());
            command.executeUpdate();
        } catch (SQLException excecao_sql) {
            excecao_sql.printStackTrace();
        } finally {
            closeResultSetAndStatement(result, command);
        }

    }

    private String sqlUpdate() {
        return """
                
                UPDATE naturalpeople\s
                SET cpf = ? , age = ?\s
                WHERE idPerson = ? ;""";
    }

    @Override
    public NaturalPersonEntity create(NaturalPersonEntity person) {
        NaturalPersonEntity newPerson = person;
        ResultSet result = null;
        PreparedStatement command = null;
        if (person.getCpf() == null){
            return null;
        }
        try {
            String sql = sqlCreate();
            command = db.prepareStatement(sql);
            command.setInt(1, newPerson.getId());
            command.setString(2, newPerson.getCpf());
            command.setInt(3, newPerson.getAge());
            command.executeUpdate();
        } catch (SQLException excecao_sql) {
            excecao_sql.printStackTrace();
        } finally {
            closeResultSetAndStatement(result, command);
        }

        return newPerson;
    }

    private String sqlCreate() {
        return "INSERT INTO naturalpeople (\"idPerson\", cpf, age) VALUES (?, ?, ?)";
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
                DELETE FROM naturalpeople WHERE "idPerson" = ?;""";
    }

    @Override
    public NaturalPersonEntity loginPerson(LoginDto req) {
        String sql = sqlQueryGetByEmailAndPassword();
        NaturalPersonEntity person = null;
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            command = db.prepareStatement(sql);
            command.setString(1, req.getEmail());
            command.setString(2, req.getSenha());
            result = command.executeQuery();
            if (result != null && result.next()) {
                person = createNaturalPersonEntity(result);
            }

        } catch (SQLException excecao_sql) {
            excecao_sql.printStackTrace();
        } finally {
            closeResultSetAndStatement(result, command);
        }

        return person;
    }

    private String sqlQueryGetByEmailAndPassword() {
        return """
                
                SELECT np.cpf, np.age,p.email,p.description,p.password,p.name,p.id,p.address,
                ad.country, ad.state, ad.cep\s
                FROM naturalpeople np\s
                INNER JOIN people p\s
                ON np."idPerson" = p.id\s
                INNER JOIN address ad\s
                ON ad.id = p.address WHERE p.email=? AND p.password=? LIMIT 1;""";
    }

    private NaturalPersonEntity createNaturalPersonEntity(ResultSet result) throws SQLException {
        Integer id = result.getInt("id");
        String email = result.getString("email");
        String name = result.getString("name");
        String password = result.getString("password");
        String description = result.getString("description");
        String cpf = result.getString("cpf");
        Integer age = result.getInt("age");
        Integer idAddress = result.getInt("address");
        String country = result.getString("country");
        String cep = result.getString("cep");
        String state = result.getString("state");
        return new NaturalPersonEntity(name, email, password, description, new AddressEntity(country, state, cep, idAddress), cpf, age, id, new ArrayList<SkillEntity>());
    }

    private void closeResultSetAndStatement(ResultSet result, PreparedStatement statement) {
        try {
            if (result != null) {
                result.close();
            }

        } catch (SQLException excecao_sql) {
            excecao_sql.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }

            } catch (SQLException excecao_sql) {
                excecao_sql.printStackTrace();
            }

        }

    }

    private Connection db;
}
