package org.example.services;

import static org.junit.jupiter.api.Assertions.*;

import org.example.dao.AddressDao;
import org.example.dao.JobDao;
import org.example.dao.LegalPersonDao;
import org.example.dao.PersonDao;
import org.example.dao.impl.AddressDaoImpl;
import org.example.dao.impl.JobDaoImpl;
import org.example.dao.impl.LegalPersonDaoImpl;
import org.example.dao.impl.PersonDaoImpl;
import org.example.entity.AddressEntity;
import org.example.entity.JobEntity;
import org.example.entity.LegalPersonEntity;
import org.example.services.impl.JobsServiceImpl;
import org.example.services.impl.LegalPersonServiceImpl;
import org.example.utils.DBTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

public class JobsServiceTest {

    private Connection conn;
    private JobDao jobDao;
    private LegalPersonDao legalPersonDao;
    private PersonDao personDao;
    private AddressDao addressDao;
    private JobsServiceImpl jobsService;
    private LegalPersonService legalPersonService;

    @BeforeEach
    public void setup() throws Exception {
        conn = DBTest.getConnection();
        personDao = new PersonDaoImpl(conn);
        jobDao = new JobDaoImpl(conn);
        legalPersonDao = new LegalPersonDaoImpl(conn);
        addressDao = new AddressDaoImpl(conn);
        jobsService = new JobsServiceImpl(jobDao, legalPersonDao, addressDao);
        legalPersonService = new LegalPersonServiceImpl(legalPersonDao,addressDao,personDao);
    }

    @Test
    public void shouldAddAndRetrieveJob() throws Exception {
        AddressEntity address = new AddressEntity("City", "State", "12345678");
        LegalPersonEntity legalPerson = new LegalPersonEntity(
                "Legal Entity Ltd", "contact@company.com", "password", "Description", address, "12345678901234"
        );
        LegalPersonEntity createdPerson = legalPersonService.addUser(legalPerson); // Create the legal person

        JobEntity job = new JobEntity();
        job.setPerson(createdPerson); // Associate the job with the legal person
        job.setName("Job Title");
        job.setDescription("Job Description");
        job.setLocal(address);

        jobsService.addJob(job); // Add the job

        JobEntity retrievedJob = jobsService.oneById(job.getId()); // Retrieve the job by ID
        assertNotNull(retrievedJob);
        assertEquals("Job Title", retrievedJob.getName());
        assertEquals("Job Description", retrievedJob.getDescription());
        assertEquals("City", retrievedJob.getLocal().getCountry());
        assertEquals("12345678901234", retrievedJob.getPerson().getCnpj());
    }

    @Test
    public void shouldUpdateJob() throws Exception {
        AddressEntity address = new AddressEntity("City", "State", "12345678");
        LegalPersonEntity legalPerson = new LegalPersonEntity(
                "Legal Entity Ltd", "contact@company.com", "password", "Description", address, "12345678901234"
        );
        LegalPersonEntity createdPerson = legalPersonService.addUser(legalPerson); // Create the legal person

        JobEntity job = new JobEntity();
        job.setPerson(legalPerson);
        job.setName("Job Title");
        job.setDescription("Job Description");
        job.setLocal(address);

        JobEntity new_job = jobsService.addJob(job); // Add the job
        job = jobsService.oneById(new_job.getId());

        job.setName("Updated Job Title");
        job.setDescription("Updated Job Description");
        job.getLocal().setCountry("New City");
        job.getLocal().setState("New State");
        job.getLocal().setCep("87654321");

        jobsService.updateById(job); // Update the job

        JobEntity updatedJob = jobsService.oneById(job.getId()); // Retrieve the updated job

        assertEquals("Updated Job Title", updatedJob.getName());
        assertEquals("Updated Job Description", updatedJob.getDescription());
        assertEquals("New City", updatedJob.getLocal().getCountry());
        assertEquals("87654321", updatedJob.getLocal().getCep());
    }

    @Test
    public void shouldDeleteJob() throws Exception {
        AddressEntity address =new AddressEntity("City", "State", "12345678");
        LegalPersonEntity legalPerson = new LegalPersonEntity(
                "Legal Entity Ltd", "contact@company.com", "password", "Description", address, "12345678901234"
        );
        LegalPersonEntity createdPerson = legalPersonService.addUser(legalPerson); // Create the legal person

        JobEntity job = new JobEntity();
        job.setPerson(legalPerson);
        job.setName("Job Title");
        job.setDescription("Job Description");
        job.setLocal(address);

        jobsService.addJob(job); // Add the job
        job = jobsService.oneById(job.getId());

        jobsService.deleteById(job); // Delete the job

        JobEntity deletedJob = jobsService.oneById(job.getId()); // Try to retrieve the deleted job
        assertNull(deletedJob); // Ensure that the job is deleted
    }

    @Test
    public void shouldFailWhenJobHasNoPerson() throws Exception {
        AddressEntity address = new AddressEntity("City", "State", "12345678");
        JobEntity job = new JobEntity();
        job.setName("Job Title");
        job.setDescription("Job Description");
        job.setLocal(address);

        assertThrows(Exception.class, () -> {
            jobsService.addJob(job); // Attempt to add a job without a person
        });
    }

    @Test
    public void shouldListAllJobs() throws Exception {
        // Criando instâncias de AddressEntity e LegalPersonEntity
        AddressEntity address = new AddressEntity("City", "State", "12345678");
        LegalPersonEntity legalPerson1 = new LegalPersonEntity("Legal Entity Ltd", "contact@company.com", "password", "Description", address, "12345678901234");
        LegalPersonEntity legalPerson2 = new LegalPersonEntity("Other Entity Ltd", "other@company.com", "password", "Description", address, "98765432100123");

        // Salvando as entidades no banco de dados
        LegalPersonEntity createdPerson1 = legalPersonService.addUser(legalPerson1);
        LegalPersonEntity createdPerson2 = legalPersonService.addUser(legalPerson2);

        // Criando instâncias de JobEntity associadas às LegalPersonEntity
        JobEntity job1 = new JobEntity("Job Title 1", "Job Description 1", address, createdPerson1);
        JobEntity job2 = new JobEntity("Job Title 2", "Job Description 2", address, createdPerson2);

        // Salvando as jobs no banco de dados
        jobsService.addJob(job1);
        jobsService.addJob(job2);

        // Executando o método listAll()
        List<JobEntity> result = jobsService.listAll();

        // Verificando os resultados
        assertNotNull(result);
        assertEquals(2, result.size()); // Espera-se que a lista tenha 2 jobs

        // Verificando os detalhes dos jobs
        assertEquals("Job Title 1", result.get(0).getName());
        assertEquals("Job Title 2", result.get(1).getName());

        // Verificando se as pessoas legais estão associadas corretamente
        assertEquals("Legal Entity Ltd", result.get(0).getPerson().getName());
        assertEquals("Other Entity Ltd", result.get(1).getPerson().getName());
    }

    @Test
    public void shouldListJobsByPerson() throws Exception {
        // Criando instâncias de AddressEntity e LegalPersonEntity
        AddressEntity address = new AddressEntity("City", "State", "12345678");
        LegalPersonEntity legalPerson = new LegalPersonEntity("Legal Entity Ltd", "contact@company.com", "password", "Description", address, "12345678901234");

        // Salvando a LegalPersonEntity no banco de dados
        LegalPersonEntity createdPerson = legalPersonService.addUser(legalPerson);

        // Criando instâncias de JobEntity associadas à LegalPersonEntity
        JobEntity job1 = new JobEntity("Job Title 1", "Job Description 1", address, createdPerson);
        JobEntity job2 = new JobEntity("Job Title 2", "Job Description 2", address, createdPerson);

        // Salvando os jobs no banco de dados
        jobsService.addJob(job1);
        jobsService.addJob(job2);

        // Executando o método listByPerson()
        List<JobEntity> result = jobsService.listByPerson(createdPerson.getId());

        // Verificando os resultados
        assertNotNull(result);
        assertEquals(2, result.size()); // Espera-se que a lista tenha 2 jobs

        // Verificando os detalhes dos jobs
        assertEquals("Job Title 1", result.get(0).getName());
        assertEquals("Job Title 2", result.get(1).getName());
    }


    @AfterEach
    public void cleanup() throws Exception {
        conn.close(); // Close the connection after each test
    }
}
