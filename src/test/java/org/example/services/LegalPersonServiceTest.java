package org.example.services;

import static org.junit.jupiter.api.Assertions.*;

import org.example.dao.AddressDao;
import org.example.dao.LegalPersonDao;
import org.example.dao.PersonDao;
import org.example.dao.impl.AddressDaoImpl;
import org.example.dao.impl.LegalPersonDaoImpl;
import org.example.dao.impl.PersonDaoImpl;
import org.example.entity.AddressEntity;
import org.example.entity.LegalPersonEntity;
import org.example.services.impl.LegalPersonServiceImpl;
import org.example.utils.DBTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

public class LegalPersonServiceTest {

    private Connection conn;
    private LegalPersonDao legalPersonDao;
    private AddressDao addressDao;
    private PersonDao personDao;

    @BeforeEach
    public void setup() throws Exception {
        conn = DBTest.getConnection();
        legalPersonDao = new LegalPersonDaoImpl(conn);
        addressDao = new AddressDaoImpl(conn);
        personDao = new PersonDaoImpl(conn);
    }

    @Test
    public void shouldAddAndRetrieveLegalPerson() throws Exception {
        LegalPersonServiceImpl legalPersonService = new LegalPersonServiceImpl(legalPersonDao, addressDao, personDao);
        AddressEntity address = new AddressEntity("Country", "State", "12345678");
        LegalPersonEntity person = new LegalPersonEntity(
                "Legal Entity Ltd",
                "email@example.com",
                "password",
                "Corporate Description",
                address,
                "11111111111111"
        );

        legalPersonService.addUser(person);
        LegalPersonEntity retrievedPerson = legalPersonService.oneById(16);
        List<LegalPersonEntity> people = legalPersonService.listAll();

        assertEquals(16, retrievedPerson.getId());
        assertEquals("email@example.com", retrievedPerson.getEmail());
        assertEquals("Legal Entity Ltd", retrievedPerson.getName());
        assertEquals("Corporate Description", retrievedPerson.getDescription());
        assertEquals("Country", retrievedPerson.getAddress().getCountry());
        assertEquals("State", retrievedPerson.getAddress().getState());
        assertEquals("12345678", retrievedPerson.getAddress().getCep());
        assertEquals("11111111111111", retrievedPerson.getCnpj());
    }

    @Test
    public void shouldUpdateLegalPerson() throws Exception {
        LegalPersonServiceImpl legalPersonService = new LegalPersonServiceImpl(legalPersonDao, addressDao, personDao);
        AddressEntity address = new AddressEntity("Country", "State", "12345678");
        LegalPersonEntity person = new LegalPersonEntity(
                "Legal Entity Ltd",
                "email@example.com",
                "password",
                "Corporate Description",
                address,
                "11111111111111"
        );

        legalPersonService.addUser(person);
        person = legalPersonService.oneById(1);

        person.setName("Updated Legal Entity");
        person.setDescription("Updated Description");
        person.getAddress().setCountry("Updated Country");
        person.getAddress().setState("Updated State");
        person.getAddress().setCep("87654321");
        person.setCnpj("22222222222222");

        legalPersonService.updateById(person);
        LegalPersonEntity updatedPerson = legalPersonService.oneById(1);

        assertEquals("Updated Legal Entity", updatedPerson.getName());
        assertEquals("Updated Description", updatedPerson.getDescription());
        assertEquals("Updated Country", updatedPerson.getAddress().getCountry());
        assertEquals("Updated State", updatedPerson.getAddress().getState());
        assertEquals("87654321", updatedPerson.getAddress().getCep());
        assertEquals("22222222222222", updatedPerson.getCnpj());
    }

    @Test
    public void shouldDeleteLegalPerson() throws Exception {
        LegalPersonServiceImpl legalPersonService = new LegalPersonServiceImpl(legalPersonDao, addressDao, personDao);
        AddressEntity address = new AddressEntity("Country", "State", "12345678");
        LegalPersonEntity person = new LegalPersonEntity(
                "Legal Entity Ltd",
                "email@example.com",
                "password",
                "Corporate Description",
                address,
                "11111111111111"
        );

        legalPersonService.addUser(person);
        person = legalPersonService.oneById(16);

        legalPersonService.deleteById(person);
        LegalPersonEntity deletedPerson = legalPersonService.oneById(16);

        assertNull(deletedPerson);
    }

    @Test
    public void shouldFailWhenCnpjIsInvalid() throws Exception {
        LegalPersonServiceImpl legalPersonService = new LegalPersonServiceImpl(legalPersonDao, addressDao, personDao);
        AddressEntity address = new AddressEntity("Country", "State", "12345678");
        LegalPersonEntity person = new LegalPersonEntity(
                "Legal Entity Ltd",
                "email@example.com",
                "password",
                "Corporate Description",
                address,
                "123456"  // CNPJ inválido
        );

        Exception exception = assertThrows(Exception.class, () -> {
            legalPersonService.addUser(person);
        });
        assertTrue(exception.getMessage().contains("O CNPJ deve conter exatamente 14 dígitos"));
    }

    @Test
    public void shouldFailWhenEmailIsInvalid() throws Exception {
        LegalPersonServiceImpl legalPersonService = new LegalPersonServiceImpl(legalPersonDao, addressDao, personDao);
        AddressEntity address = new AddressEntity("Country", "State", "12345678");
        LegalPersonEntity person = new LegalPersonEntity(
                "Legal Entity Ltd",
                "invalid-email",  // E-mail inválido
                "password",
                "Corporate Description",
                address,
                "11111111111111"
        );

        Exception exception = assertThrows(Exception.class, () -> {
            legalPersonService.addUser(person);
        });
        assertTrue(exception.getMessage().contains("O e-mail deve ser válido"));
    }

    @Test
    public void shouldFailWhenNameIsBlank() throws Exception {
        LegalPersonServiceImpl legalPersonService = new LegalPersonServiceImpl(legalPersonDao, addressDao, personDao);
        AddressEntity address = new AddressEntity("Country", "State", "12345678");
        LegalPersonEntity person = new LegalPersonEntity(
                "",  // Nome vazio
                "email@example.com",
                "password",
                "Corporate Description",
                address,
                "11111111111111"
        );

        Exception exception = assertThrows(Exception.class, () -> {
            legalPersonService.addUser(person);
        });
        assertTrue(exception.getMessage().contains("O nome não pode estar vazio"));
    }

    @AfterEach
    public void cleanup() throws Exception {
        conn.close();
    }
}
