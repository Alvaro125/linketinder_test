package org.example.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.example.dao.impl.JobDaoImpl;
import org.example.entity.AddressEntity;
import org.example.entity.JobEntity;
import org.example.entity.LegalPersonEntity;
import org.example.utils.DBTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class JobDaoTest {

    private static Connection connection;
    private JobDaoImpl jobDao;

    @BeforeAll
    static void setUpDatabase() throws SQLException {
        connection = DBTest.getConnection(); // Conexão para banco de dados de teste
    }

    @BeforeEach
    void setUp() {
        jobDao = new JobDaoImpl(connection);
    }

    @AfterAll
    static void tearDownDatabase() throws SQLException {
        DBTest.closeConnection(connection);
    }

    @Test
    void testCreateJob() {
        AddressEntity address = new AddressEntity("Brazil", "MS", "79800-000");
        address.setId(1); // Assumindo que o endereço com ID 1 existe no banco

        LegalPersonEntity legalPerson = new LegalPersonEntity();
        legalPerson.setId(5); // Assumindo que a pessoa jurídica com ID 1 existe no banco
        legalPerson.setName("Empresa de Teste");
        legalPerson.setCnpj("12.345.678/0001-99");

        JobEntity job = new JobEntity("Software Developer", "Develop and maintain software", address, legalPerson, null);
        JobEntity createdJob = jobDao.create(job);

        assertNotNull(createdJob, "A entidade criada não deve ser nula");
        assertEquals("Software Developer", createdJob.getName());
        assertEquals("Brazil", createdJob.getLocal().getCountry());
    }

    @Test
    void testGetAllJobs() {
        List<JobEntity> jobs = jobDao.getAll();

        assertNotNull(jobs, "A lista de trabalhos não deve ser nula");
        assertTrue(jobs.size() > 0, "A lista de trabalhos deve conter elementos");

        // Verifica se o toString está correto para o primeiro trabalho da lista
        JobEntity job = jobs.get(0);
        assertNotNull(job.toString(), "O método toString() não deve retornar nulo");
    }

    @Test
    void testGetJobById() {
        Integer existingJobId = 1; // Substitua pelo ID de um trabalho existente no banco de dados
        JobEntity job = jobDao.getById(existingJobId);

        assertNotNull(job, "O trabalho recuperado não deve ser nulo");
        assertEquals(existingJobId, job.getId());

        // Verifica se o toString() inclui informações do endereço e pessoa jurídica
        String jobString = job.toString();
        assertTrue(jobString.contains("Nome da Vaga:"), "O toString() deve conter o nome da vaga");
        assertTrue(jobString.contains("Empresa:"), "O toString() deve conter informações da empresa");
        assertTrue(jobString.contains("Endereço:"), "O toString() deve conter informações do endereço");
    }

    @Test
    void testUpdateJob() {
        AddressEntity address = new AddressEntity("Brazil", "MS", "79800-000");
        address.setId(1); // Assumindo que o endereço com ID 1 existe no banco

        LegalPersonEntity legalPerson = new LegalPersonEntity();
        legalPerson.setId(1); // Assumindo que a pessoa jurídica com ID 1 existe no banco
        legalPerson.setName("Empresa de Teste");
        legalPerson.setCnpj("12.345.678/0001-99");

        JobEntity job = new JobEntity("Software Developer", "Develop and maintain software", address, legalPerson, null);

        JobEntity createdJob = jobDao.create(job);
        JobEntity jobSearch = jobDao.getById(createdJob.getId());

        assertNotNull(jobSearch, "O trabalho recuperado não deve ser nulo");

        createdJob.setName("Updated Job Name");
        createdJob.setDescription("Updated Job Description");

        jobDao.updateById(createdJob);

        JobEntity updatedJob = jobDao.getById(createdJob.getId());

        assertNotNull(updatedJob, "O trabalho atualizado não deve ser nulo");
        assertEquals("Updated Job Name", updatedJob.getName());
        assertEquals("Updated Job Description", updatedJob.getDescription());
    }

    @Test
    void testDeleteJob() {
        AddressEntity address = new AddressEntity("Argentina", "BA", "C1002");
        address.setId(2); // Assumindo que o endereço com ID 2 existe no banco

        LegalPersonEntity legalPerson = new LegalPersonEntity();
        legalPerson.setId(1);
        legalPerson.setName("Empresa Temporária");
        legalPerson.setCnpj("98.765.432/0001-11");

        JobEntity job = new JobEntity("Temporary Job", "Temporary job description", address, legalPerson, null);

        JobEntity createdJob = jobDao.create(job);

        assertNotNull(createdJob, "O trabalho criado não deve ser nulo");
        Integer createdJobId = createdJob.getId();

        jobDao.deleteById(createdJobId);

        JobEntity deletedJob = jobDao.getById(createdJobId);

        assertNull(deletedJob, "O trabalho deve ser nulo após a deleção");
    }
}
