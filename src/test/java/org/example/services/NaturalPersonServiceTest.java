package org.example.services;

import static org.junit.jupiter.api.Assertions.*;

import org.example.dao.AddressDao;
import org.example.dao.NaturalPersonDao;
import org.example.dao.PersonDao;
import org.example.dao.impl.AddressDaoImpl;
import org.example.dao.impl.NaturalPersonDaoImpl;
import org.example.dao.impl.PersonDaoImpl;
import org.example.entity.AddressEntity;
import org.example.entity.NaturalPersonEntity;
import org.example.services.NaturalPersonService;
import org.example.services.impl.NaturalPersonServiceImpl;
import org.example.utils.DBTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

public class NaturalPersonServiceTest {

    private Connection conn;
    private NaturalPersonDao naturalPersonDao;
    private AddressDao addressDao;
    private PersonDao personDao;

    @BeforeEach
    public void setup() throws Exception {
        conn = DBTest.getConnection();
        naturalPersonDao = new NaturalPersonDaoImpl(conn);
        addressDao = new AddressDaoImpl(conn);
        personDao = new PersonDaoImpl(conn);
    }

    @Test
    public void shouldAddAndRetrieveNaturalPerson() throws Exception {
        NaturalPersonService naturalPersonService = new NaturalPersonServiceImpl(naturalPersonDao, addressDao, personDao);
        AddressEntity address = new AddressEntity("Country", "State", "12345678");
        NaturalPersonEntity person = new NaturalPersonEntity(
                "John Doe",
                "email@example.com",
                "password",
                "Description",
                address,
                "12345678901",
                30,
                0
        );

        naturalPersonService.addUser(person);
        NaturalPersonEntity retrievedPerson = naturalPersonService.oneById(1);
        List<NaturalPersonEntity> people = naturalPersonService.listAll();

        assertEquals(6, people.size());
        NaturalPersonEntity lastPerson = people.get(people.size() - 1);
        assertEquals("email@example.com", lastPerson.getEmail());
        assertEquals("John Doe", lastPerson.getName());
        assertEquals("Description", lastPerson.getDescription());
        assertEquals("Country", lastPerson.getAddress().getCountry());
        assertEquals("State", lastPerson.getAddress().getState());
        assertEquals("12345678", lastPerson.getAddress().getCep());
        assertEquals("12345678901", lastPerson.getCpf());
        assertEquals(30, lastPerson.getAge());
    }

    @Test
    public void shouldUpdateNaturalPerson() throws Exception {
        NaturalPersonService naturalPersonService = new NaturalPersonServiceImpl(naturalPersonDao, addressDao, personDao);
        AddressEntity address = new AddressEntity("Country", "State", "12345678");
        NaturalPersonEntity person = new NaturalPersonEntity(
                "John Doe",
                "email@example.com",
                "password",
                "Description",
                address,
                "12345678901",
                30,
                16
        );

        naturalPersonService.addUser(person);
        person = naturalPersonService.oneById(16);

        person.setName("Jane Doe");
        person.setDescription("New Description");
        person.getAddress().setCountry("New Country");
        person.getAddress().setState("New State");
        person.getAddress().setCep("87654321");
        person.setCpf("10987654321");
        person.setAge(40);

        naturalPersonService.updateById(person);
        NaturalPersonEntity updatedPerson = naturalPersonService.oneById(16);

        assertEquals(16, updatedPerson.getId());
        assertEquals("email@example.com", updatedPerson.getEmail());
        assertEquals("Jane Doe", updatedPerson.getName());
        assertEquals("New Description", updatedPerson.getDescription());
        assertEquals("New Country", updatedPerson.getAddress().getCountry());
        assertEquals("New State", updatedPerson.getAddress().getState());
        assertEquals("87654321", updatedPerson.getAddress().getCep());
        assertEquals("10987654321", updatedPerson.getCpf());
        assertEquals(40, updatedPerson.getAge());
    }

    @Test
    public void shouldDeleteNaturalPerson() throws Exception {
        NaturalPersonService naturalPersonService = new NaturalPersonServiceImpl(naturalPersonDao, addressDao, personDao);
        AddressEntity address = new AddressEntity("Country", "State", "12345678");
        NaturalPersonEntity person = new NaturalPersonEntity(
                "John Doe",
                "email@example.com",
                "password",
                "Description",
                address,
                "12345678901",
                30,
                0
        );

        naturalPersonService.addUser(person);
        person = naturalPersonService.oneById(9);

        naturalPersonService.deleteById(person);
        NaturalPersonEntity deletedPerson = naturalPersonService.oneById(11);

        assertNull(deletedPerson);
    }

    @AfterEach
    public void cleanup() throws Exception {
        conn.close();
    }
}
