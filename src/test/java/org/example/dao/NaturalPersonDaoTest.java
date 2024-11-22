package org.example.dao;

import org.example.dao.impl.AddressDaoImpl;
import org.example.dao.impl.NaturalPersonDaoImpl;
import org.example.dao.impl.PersonDaoImpl;
import org.example.dto.LoginDto;
import org.example.entity.AddressEntity;
import org.example.entity.NaturalPersonEntity;
import org.example.entity.PersonEntity;
import org.example.utils.DBTest;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NaturalPersonDaoTest {

    private static Connection connection;
    private NaturalPersonDaoImpl naturalPersonDao;
    private AddressEntity testAddress;
    private PersonDaoImpl personDao;

    @BeforeAll
    static void setUpDatabase() throws SQLException {
        connection = DBTest.getConnection();
    }

    @BeforeEach
    void setUp() throws SQLException {
        naturalPersonDao = new NaturalPersonDaoImpl(connection);
        personDao = new PersonDaoImpl(connection);
        testAddress = createTestAddress();
    }

    @AfterAll
    static void tearDownDatabase() throws SQLException {
        DBTest.closeConnection(connection);
    }

    private AddressEntity createTestAddress() throws SQLException {
        AddressEntity address = new AddressEntity();
        address.setCountry("Brasil");
        address.setState("São Paulo");
        address.setCep("01001000");

        return new AddressDaoImpl(connection).create(address);
    }

    private NaturalPersonEntity createTestNaturalPerson() {
        return new NaturalPersonEntity(
                "João Silva",
                "joao@teste.com",
                "senha123",
                "Descrição de João Silva",
                testAddress,
                "12345678901",
                25,
                null,
                new ArrayList<>()
        );
    }

    @Test
    void testCreateNaturalPerson() {
        NaturalPersonEntity personNatural = createTestNaturalPerson();
        PersonEntity person = personDao.create(new PersonEntity(
                personNatural.getName(),
                personNatural.getEmail(),
                personNatural.getPassword(),
                personNatural.getDescription(),
                personNatural.getAddress(),
                personNatural.getId()));
        personNatural.setId(person.getId());
        NaturalPersonEntity createdPerson = naturalPersonDao.create(personNatural);

        assertNotNull(createdPerson, "A pessoa física criada não deve ser nula");
        assertNotNull(createdPerson.getId(), "O ID da pessoa física não deve ser nulo");
        assertEquals("12345678901", createdPerson.getCpf(), "O CPF deve corresponder ao esperado");
        assertEquals("João Silva", createdPerson.getName(), "O nome deve corresponder ao esperado");
    }

    @Test
    void testGetAll() {
        NaturalPersonEntity personNatural1 = createTestNaturalPerson();
        personNatural1.setCpf("38755432100");
        PersonEntity person1 = personDao.create(new PersonEntity(
                personNatural1.getName(),
                personNatural1.getEmail(),
                personNatural1.getPassword(),
                personNatural1.getDescription(),
                personNatural1.getAddress(),
                personNatural1.getId()));
        personNatural1.setId(person1.getId());
        NaturalPersonEntity personNatural2 = createTestNaturalPerson();
        personNatural2.setCpf("98765432100");
        personNatural2.setName("Maria Oliveira");
        PersonEntity person2 = personDao.create(new PersonEntity(
                personNatural2.getName(),
                personNatural2.getEmail(),
                personNatural2.getPassword(),
                personNatural2.getDescription(),
                personNatural2.getAddress(),
                personNatural2.getId()));
        personNatural2.setId(person2.getId());

        naturalPersonDao.create(personNatural1);
        naturalPersonDao.create(personNatural2);

        List<NaturalPersonEntity> persons = naturalPersonDao.getAll();

        assertFalse(persons.isEmpty(), "A lista de pessoas físicas não deve estar vazia");
        assertTrue(persons.size() >= 2, "Deve haver pelo menos 2 pessoas físicas na lista");
    }

    @Test
    void testGetById() {
        NaturalPersonEntity personNatural = createTestNaturalPerson();
        personNatural.setCpf("98755432100");
        PersonEntity person = personDao.create(new PersonEntity(
                personNatural.getName(),
                personNatural.getEmail(),
                personNatural.getPassword(),
                personNatural.getDescription(),
                personNatural.getAddress(),
                personNatural.getId()));
        personNatural.setId(person.getId());
        NaturalPersonEntity createdPerson = naturalPersonDao.create(personNatural);

        NaturalPersonEntity retrievedPerson = naturalPersonDao.getById(createdPerson.getId());

        assertNotNull(retrievedPerson, "A pessoa física recuperada não deve ser nula");
        assertEquals(createdPerson.getId(), retrievedPerson.getId(), "Os IDs devem corresponder");
        assertEquals(createdPerson.getCpf(), retrievedPerson.getCpf(), "Os CPFs devem corresponder");
    }

    @Test
    void testUpdateById() {
        NaturalPersonEntity personNatural = createTestNaturalPerson();
        personNatural.setCpf("98765432333");
        PersonEntity person = personDao.create(new PersonEntity(
                personNatural.getName(),
                personNatural.getEmail(),
                personNatural.getPassword(),
                personNatural.getDescription(),
                personNatural.getAddress()));
        personNatural.setId(person.getId());
        NaturalPersonEntity createdPerson = naturalPersonDao.create(personNatural);

        createdPerson.setCpf("11111111111");
        createdPerson.setAge(30);
        naturalPersonDao.updateById(createdPerson);

        NaturalPersonEntity updatedPerson = naturalPersonDao.getById(createdPerson.getId());
        assertEquals("11111111111", updatedPerson.getCpf(), "O CPF deve ter sido atualizado");
        assertEquals(30, updatedPerson.getAge(), "A idade deve ter sido atualizada");
    }

    @Test
    void testDeleteById() {
        NaturalPersonEntity personNatural = createTestNaturalPerson();
        personNatural.setCpf("98765431133");
        PersonEntity person = personDao.create(new PersonEntity(
                personNatural.getName(),
                personNatural.getEmail(),
                personNatural.getPassword(),
                personNatural.getDescription(),
                personNatural.getAddress()));
        personNatural.setId(person.getId());
        NaturalPersonEntity createdPerson = naturalPersonDao.create(personNatural);

        naturalPersonDao.deleteById(createdPerson.getId());

        NaturalPersonEntity deletedPerson = naturalPersonDao.getById(createdPerson.getId());
        assertNull(deletedPerson, "A pessoa física deve ter sido deletada");
    }

    @Test
    void testLoginPerson_Success() {
        NaturalPersonEntity personNatural = createTestNaturalPerson();
        personNatural.setEmail("joao@teste.com");
        personNatural.setPassword("senha123");
        personNatural.setCpf("98765432444");
        PersonEntity person = personDao.create(new PersonEntity(
                personNatural.getName(),
                personNatural.getEmail(),
                personNatural.getPassword(),
                personNatural.getDescription(),
                personNatural.getAddress()));
        personNatural.setId(person.getId());
        NaturalPersonEntity createdPerson = naturalPersonDao.create(personNatural);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("joao@teste.com");
        loginDto.setSenha("senha123");

        NaturalPersonEntity loggedPerson = naturalPersonDao.loginPerson(loginDto);

        assertNotNull(loggedPerson, "O login deve retornar uma pessoa física");
        assertEquals(createdPerson.getName(), loggedPerson.getName(), "Os Names devem corresponder");
    }

    @Test
    void testLoginPerson_Failure() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("invalido@teste.com");
        loginDto.setSenha("senhaerrada");

        NaturalPersonEntity loggedPerson = naturalPersonDao.loginPerson(loginDto);

        assertNull(loggedPerson, "O login deve falhar para credenciais inválidas");
    }

    @Test
    void testCreateNaturalPersonWithInvalidData() {
        NaturalPersonEntity invalidPerson = createTestNaturalPerson();
        PersonEntity person = personDao.create(new PersonEntity(
                invalidPerson.getName(),
                invalidPerson.getEmail(),
                invalidPerson.getPassword(),
                invalidPerson.getDescription(),
                invalidPerson.getAddress()));
        invalidPerson.setId(person.getId());
        invalidPerson.setCpf(null); // CPF nulo para testar validação
        invalidPerson.setName(""); // Nome vazio para testar validação

        NaturalPersonEntity result = naturalPersonDao.create(invalidPerson);
        assertNull(result, "Não deve criar pessoa física com dados inválidos");
    }

    @Test
    void testGetByIdWithInvalidId() {
        NaturalPersonEntity result = naturalPersonDao.getById(-1);
        assertNull(result, "Deve retornar null para ID inválido");
    }
}
