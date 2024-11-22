package org.example.dao.impl;

import org.example.dao.JobDao;
import org.example.entity.AddressEntity;
import org.example.entity.JobEntity;
import org.example.entity.LegalPersonEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDaoImpl implements JobDao {
    public JobDaoImpl(Connection db) {
        this.db = db;
    }

    @Override
    public List<JobEntity> getAll() {
        String sql = sqlQueryGetAll();
        ResultSet result = null;
        PreparedStatement command = null;
        ArrayList<JobEntity> list = new ArrayList();
        try {
            command = db.prepareStatement(sql);
            result = command.executeQuery();
            while (result.next()) {
                list.add(createJobEntity(result));
            }

        } catch (SQLException excecao_sql) {
            excecao_sql.printStackTrace();
        } finally {
            closeResultSetAndStatement(result, command);
        }

        return list;
    }

    private String sqlQueryGetAll() {
        return """
                SELECT jobs.id, jobs."idLegalPerson" ,jobs.name,jobs.description,
                jobs.local,address.country,address.state,lp.cnpj,p.name as namelp,address.cep FROM jobs
                INNER JOIN address
                ON address.id = jobs.local\s
                INNER JOIN legalpeople lp
                ON lp.idPerson = jobs.idLegalPerson\s
                INNER JOIN people p
                ON p.id = jobs.idLegalPerson;""";
    }

    @Override
    public List<JobEntity> getByIdPerson(Integer idPerson) {
        String sql = sqlQueryGetByIdPerson();
        ResultSet result = null;
        PreparedStatement command = null;
        ArrayList<JobEntity> list = new ArrayList();
        try {
            command = db.prepareStatement(sql);
            command.setInt(1, idPerson);
            result = command.executeQuery();
            while (result.next()) {
                list.add(createJobEntity(result));
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
                SELECT jobs.id, jobs."idLegalPerson" ,jobs.name,jobs.description,
                jobs."local",address.country,lp.cnpj,p.name as namelp, address.state,address.cep FROM jobs
                INNER JOIN address
                ON address.id = jobs."local"\s
                INNER JOIN legalpeople lp
                ON lp.idPerson = jobs.idLegalPerson\s
                INNER JOIN people p
                ON p.id = jobs.idLegalPerson\s
                WHERE jobs."idLegalPerson" = ?;\s""";
    }

    @Override
    public JobEntity getById(Integer id) {
        String sql = sqlQueryGetById();
        JobEntity job = null;
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            command = db.prepareStatement(sql);
            command.setInt(1, id);
            result = command.executeQuery();
            if (result != null && result.next()) {
                job = createJobEntity(result);
            }

        } catch (SQLException excecao_sql) {
            excecao_sql.printStackTrace();
        } finally {
            closeResultSetAndStatement(result, command);
        }

        return job;
    }

    private String sqlQueryGetById() {
        return """
                SELECT jobs.id, jobs.idLegalPerson ,jobs.name,jobs.description,
                jobs.local,address.country,address.state,address.cep, lp.cnpj,
                 p.name as namelp FROM jobs
                INNER JOIN address
                ON address.id = jobs.local\s
                INNER JOIN legalpeople lp
                ON lp.idPerson = jobs.idLegalPerson\s
                INNER JOIN people p
                ON p.id = jobs.idLegalPerson\s
                WHERE jobs.id=? LIMIT 1;""";
    }

    @Override
    public void updateById(JobEntity job) {
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            String sql = sqlUpdate();
            command = db.prepareStatement(sql);
            command.setString(1, job.getName());
            command.setString(2, job.getDescription());
            command.setInt(3, job.getId());
            command.executeUpdate();
        } catch (SQLException excecao_sql) {
            excecao_sql.printStackTrace();
        } finally {
            closeResultSetAndStatement(result, command);
        }

    }

    private String sqlUpdate() {
        return "UPDATE jobs SET name = ?, description = ? WHERE id = ?;";
    }

    @Override
    public JobEntity create(JobEntity job) {
        JobEntity newJob = job;
        ResultSet result = null;
        PreparedStatement command = null;
        try {
            String sql = sqlCreate();
            command = db.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            command.setString(1, newJob.getName());
            command.setString(2, newJob.getDescription());
            command.setInt(3, newJob.getLocal().getId());
            command.setInt(4, newJob.getPerson().getId());
            command.executeUpdate();
            ResultSet generatedKeys = command.getGeneratedKeys();
            if (generatedKeys.next()) {
                newJob.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException excecao_sql) {
            excecao_sql.printStackTrace();
        } finally {
            closeResultSetAndStatement(result, command);
        }

        return newJob;
    }

    private String sqlCreate() {
        return "INSERT INTO jobs (name, description, local, \"idLegalPerson\") VALUES (?, ?, ?, ?)";
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
        return "DELETE FROM jobs WHERE id = ?;";
    }

    private JobEntity createJobEntity(ResultSet result) throws SQLException {
        Integer id = result.getInt("id");
        Integer idPerson = result.getInt("idLegalPerson");
        Integer local = result.getInt("local");
        String name = result.getString("name");
        String description = result.getString("description");
        String country = result.getString("country");
        String cep = result.getString("cep");
        String cnpj = result.getString("cnpj");
        String namelp = result.getString("namelp");
        String state = result.getString("state");
        AddressEntity address = new AddressEntity(country, state, cep);
        address.setId(local);
        LegalPersonEntity legalPerson = new LegalPersonEntity();
        legalPerson.setName(namelp);
        legalPerson.setCnpj(cnpj);
        legalPerson.setId(idPerson);
        return new JobEntity(name, description, address, legalPerson, id);
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
