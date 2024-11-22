package org.example.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.example.dao.impl.AddressDaoImpl;
import org.example.dao.impl.PersonDaoImpl;
import org.example.dao.impl.SkillDaoImpl;
import org.example.entity.PersonEntity;
import org.example.entity.AddressEntity;
import org.example.entity.SkillEntity;
import org.example.utils.DBTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class PersonDaoTest {

    private static Connection connection;
    private PersonDaoImpl personDao;
    private AddressDaoImpl addressDao;
    private SkillDaoImpl skillDao;

    @BeforeAll
    static void setUpDatabase() throws SQLException {
        connection = DBTest.getConnection(); // Configurar conexão para o banco de dados de teste
    }

    @BeforeEach
    void setUp() {
        personDao = new PersonDaoImpl(connection);
        addressDao = new AddressDaoImpl(connection);
        skillDao = new SkillDaoImpl(connection);
    }

    @AfterAll
    static void tearDownDatabase() throws SQLException {
        DBTest.closeConnection(connection);
    }

    @Test
    void testCreatePerson() {
        AddressEntity address = new AddressEntity("Brazil", "Mato grosso do Sul", "79800010",1);
        PersonEntity person = new PersonEntity("John Doe", "john.doe@example.com", "password123", "A test user", address);

        PersonEntity createdPerson = personDao.create(person);

        assertNotNull(createdPerson, "A entidade criada não deve ser nula");
        assertNotNull(createdPerson.getId(), "O ID da pessoa criada não deve ser nulo");
        assertEquals("John Doe", createdPerson.getName());
        assertEquals("john.doe@example.com", createdPerson.getEmail());
        assertEquals("Brazil", createdPerson.getAddress().getCountry());
    }

    @Test
    void testGetById() {
        AddressEntity address = addressDao.create(new AddressEntity("USA", "CA", "90001"));
        PersonEntity person = new PersonEntity("Jane Doe22", "jane.doe22@example.com", "password456", "Another test user", address);

        PersonEntity createdPerson = personDao.create(person);
        PersonEntity retrievedPerson = personDao.getById(createdPerson.getId());

        assertNotNull(retrievedPerson, "A entidade recuperada não deve ser nula");
        assertEquals(createdPerson.getId(), retrievedPerson.getId());
        assertEquals("Jane Doe22", retrievedPerson.getName());
        assertEquals("USA", retrievedPerson.getAddress().getCountry());
    }

    @Test
    void testUpdateById() {
        AddressEntity address = addressDao.create(new AddressEntity(
                "Canada", "ON", "M5H 2N2"));
        PersonEntity person = new PersonEntity(
                "Initial Name",
                "update.test@example.com",
                "password789",
                "Initial Description",
                address);

        PersonEntity createdPerson = personDao.create(person);

        createdPerson.setName("Updated Name");
        createdPerson.setDescription("Updated Description");
        createdPerson.getAddress().setCountry("France");

        personDao.updateById(createdPerson);
        addressDao.updateById(createdPerson.getAddress());

        PersonEntity updatedPerson = personDao.getById(createdPerson.getId());
        AddressEntity updatedAddress = addressDao.getById(createdPerson.getAddress().getId());
        updatedPerson.setAddress(updatedAddress);

        assertNotNull(updatedPerson);
        assertEquals("Updated Name", updatedPerson.getName());
        assertEquals("Updated Description", updatedPerson.getDescription());
        assertEquals("France", updatedPerson.getAddress().getCountry());
    }

    @Test
    void testDeleteById() {
        AddressEntity address = addressDao.create(new AddressEntity("Argentina", "BA", "C1002"));
        PersonEntity person = new PersonEntity("To Be Deleted", "delete.test@example.com", "password1234", "Test deletion", address);

        PersonEntity createdPerson = personDao.create(person);
        personDao.deleteById(createdPerson.getId());

        PersonEntity deletedPerson = personDao.getById(createdPerson.getId());
        assertNull(deletedPerson, "A entidade deve ser nula após a deleção");
    }

    @Test
    void testCreatePersonWithSkills() {
        AddressEntity address = addressDao.create(new AddressEntity("Germany", "BE", "10117"));
        PersonEntity person = new PersonEntity("Skillful Person", "skills.test@example.com", "password5678", "Person with skills", address);

        // Adicionando habilidades
        SkillEntity skill1 = new SkillEntity("Java", "Java Development", null);
        SkillEntity skill2 = new SkillEntity("Python", "Python Development", null);

        person.addSkills(skill1);
        person.addSkills(skill2);

        PersonEntity createdPerson = personDao.create(person);

        assertNotNull(createdPerson.getSkills(), "A lista de habilidades não deve ser nula");
        assertEquals(2, createdPerson.getSkills().size(), "A pessoa deve ter 2 habilidades associadas");
    }

    @Test
    void testCreatePersonWithInvalidData() {
        AddressEntity address = new AddressEntity("Italy", "RM", "00185");
        PersonEntity invalidPerson = new PersonEntity(null, null, null, "Invalid data", address);

        assertThrows(RuntimeException.class, () -> personDao.create(invalidPerson),
                "Deve lançar exceção ao tentar criar uma pessoa com dados inválidos");
    }
}
