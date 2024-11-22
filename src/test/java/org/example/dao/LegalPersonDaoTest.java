package org.example.dao;

import org.example.dao.impl.AddressDaoImpl;
import org.example.dao.impl.LegalPersonDaoImpl;
import org.example.dao.impl.PersonDaoImpl;
import org.example.dto.LoginDto;
import org.example.entity.AddressEntity;
import org.example.entity.LegalPersonEntity;
import org.example.entity.PersonEntity;
import org.example.utils.DBTest;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LegalPersonDaoTest {

    private static Connection connection;
    private LegalPersonDaoImpl legalPersonDao;
    private AddressEntity testAddress;
    private PersonDaoImpl personDao;

    @BeforeAll
    static void setUpDatabase() throws SQLException {
        connection = DBTest.getConnection();
    }

    @BeforeEach
    void setUp() throws SQLException {
        legalPersonDao = new LegalPersonDaoImpl(connection);
        personDao = new PersonDaoImpl(connection);
        // Criar um endereço para usar nos testes
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

        // Assumindo que temos um AddressDao para criar o endereço
        return new AddressDaoImpl(connection).create(address);
    }

    private LegalPersonEntity createTestLegalPerson() {
        return new LegalPersonEntity(
                "Empresa Teste",
                "empresa@teste.com",
                "senha123",
                "Descrição da empresa teste",
                testAddress,
                "12345678901234",
                11,
                new ArrayList<>()
        );
    }

    @Test
    void testCreateLegalPerson() {
        LegalPersonEntity legalPerson = createTestLegalPerson();

        LegalPersonEntity createdPerson = legalPersonDao.create(legalPerson);

        assertNotNull(createdPerson, "A pessoa jurídica criada não deve ser nula");
        assertNotNull(createdPerson.getId(), "O ID da pessoa jurídica não deve ser nulo");
        assertEquals("12345678901234", createdPerson.getCnpj(), "O CNPJ deve corresponder ao esperado");
        assertEquals("Empresa Teste", createdPerson.getName(), "O nome deve corresponder ao esperado");
    }

    @Test
    void testGetAll() {
        // Criar múltiplas pessoas jurídicas
        LegalPersonEntity personLegal1 = createTestLegalPerson();
        LegalPersonEntity personLegal2 = createTestLegalPerson();
        personLegal2.setCnpj("98765432101234");
        personLegal2.setName("Empresa Teste 2");
        PersonEntity person1 = personDao.create(new PersonEntity(
                personLegal1.getName(),
                personLegal1.getEmail(),
                personLegal1.getPassword(),
                personLegal1.getDescription(),
                personLegal1.getAddress(),
                personLegal1.getId()));
        PersonEntity person2 = personDao.create(new PersonEntity(
                personLegal2.getName(),
                personLegal2.getEmail(),
                personLegal2.getPassword(),
                personLegal2.getDescription(),
                personLegal2.getAddress(),
                personLegal2.getId()));
        personLegal1.setId(person1.getId());
        personLegal2.setId(person2.getId());
        legalPersonDao.create(personLegal1);
        legalPersonDao.create(personLegal2);

        List<LegalPersonEntity> persons = legalPersonDao.getAll();

        assertFalse(persons.isEmpty(), "A lista de pessoas jurídicas não deve estar vazia");
        assertTrue(persons.size() >= 2, "Deve haver pelo menos 2 pessoas jurídicas na lista");
    }

    @Test
    void testGetById() {
        LegalPersonEntity createdPerson = createTestLegalPerson();
        createdPerson.setCnpj("11111111111111");

        LegalPersonEntity retrievedPerson = legalPersonDao.getById(createdPerson.getId());

        assertNotNull(retrievedPerson, "A pessoa jurídica recuperada não deve ser nula");
        assertEquals(createdPerson.getId(), retrievedPerson.getId(), "Os IDs devem corresponder");
        assertEquals(createdPerson.getCnpj(), retrievedPerson.getCnpj(), "Os CNPJs devem corresponder");
    }

    @Test
    void testUpdateById() {
        LegalPersonEntity person = createTestLegalPerson();
        LegalPersonEntity createdPerson = legalPersonDao.getById(11);

        createdPerson.setCnpj("11111111111111");
        legalPersonDao.updateById(createdPerson);

        LegalPersonEntity updatedPerson = legalPersonDao.getById(createdPerson.getId());
        assertEquals("11111111111111", updatedPerson.getCnpj(), "O CNPJ deve ter sido atualizado");
    }

    @Test
    void testDeleteById() {
        LegalPersonEntity person = createTestLegalPerson();
        person.setId(12);
        LegalPersonEntity createdPerson = legalPersonDao.create(person);

        legalPersonDao.deleteById(createdPerson.getId());

        LegalPersonEntity deletedPerson = legalPersonDao.getById(createdPerson.getId());
        assertNull(deletedPerson, "A pessoa jurídica deve ter sido deletada");
    }

    @Test
    void testLoginPerson_Success() {
        LegalPersonEntity person = createTestLegalPerson();
        person.setId(14);
        person.setCnpj("24680135000222");
        LegalPersonEntity createdPerson = legalPersonDao.create(person);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("cooper3@example.com");
        loginDto.setSenha("senha131415");

        LegalPersonEntity loggedPerson = legalPersonDao.loginPerson(loginDto);

        assertNotNull(loggedPerson, "O login deve retornar uma pessoa jurídica");
        assertEquals(createdPerson.getId(), loggedPerson.getId(), "Os IDs devem corresponder");
    }

    @Test
    void testLoginPerson_Failure() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("naoexiste@teste.com");
        loginDto.setSenha("senhaerrada");

        LegalPersonEntity loggedPerson = legalPersonDao.loginPerson(loginDto);

        assertNull(loggedPerson, "O login deve falhar para credenciais inválidas");
    }

    @Test
    void testCreateLegalPersonWithInvalidData() {
        LegalPersonEntity invalidPerson = new LegalPersonEntity();
        invalidPerson.setId(12);
        invalidPerson.setCnpj(null); // CNPJ nulo para testar validação
        invalidPerson.setName(""); // Nome vazio para testar validação

        LegalPersonEntity result = legalPersonDao.create(invalidPerson);
        assertNull(result, "Não deve criar pessoa jurídica com dados inválidos");
    }

    @Test
    void testGetByIdWithInvalidId() {
        LegalPersonEntity result = legalPersonDao.getById(-1);
        assertNull(result, "Deve retornar null para ID inválido");
    }
}