package org.example.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PersonEntityTest {

    // Concrete implementation of PersonEntity for testing
    static class TestPersonEntity extends PersonEntity {
        public TestPersonEntity(String name, String email, String password, String description, AddressEntity address, Integer id) {
            super(name, email, password, description, address, id);
        }

        public TestPersonEntity() {
            super();
        }
    }

    private PersonEntity person;
    private AddressEntity address;
    private SkillEntity skill1;
    private SkillEntity skill2;

    @BeforeEach
    void setUp() {
        address = new AddressEntity("Brazil", "São Paulo", "01000-000", 1);
        skill1 = new SkillEntity("Java", "Object-Oriented Programming",1);
        skill2 = new SkillEntity("SQL", "Database Management",2);
        person = new TestPersonEntity("John Doe", "john.doe@example.com", "password123", "A software engineer", address, 1);
    }

    @AfterEach
    void tearDown() {
        person = null;
        address = null;
        skill1 = null;
        skill2 = null;
    }

    @Test
    void getId() {
        assertEquals(1, person.getId(), "ID should be 1");
    }

    @Test
    void setId() {
        person.setId(2);
        assertEquals(2, person.getId(), "ID should be updated to 2");
    }

    @Test
    void getName() {
        assertEquals("John Doe", person.getName(), "Name should be 'John Doe'");
    }

    @Test
    void setName() {
        person.setName("Jane Doe");
        assertEquals("Jane Doe", person.getName(), "Name should be updated to 'Jane Doe'");
    }

    @Test
    void getEmail() {
        assertEquals("john.doe@example.com", person.getEmail(), "Email should be 'john.doe@example.com'");
    }

    @Test
    void setEmail() {
        person.setEmail("jane.doe@example.com");
        assertEquals("jane.doe@example.com", person.getEmail(), "Email should be updated to 'jane.doe@example.com'");
    }

    @Test
    void getPassword() {
        assertEquals("password123", person.getPassword(), "Password should be 'password123'");
    }

    @Test
    void setPassword() {
        person.setPassword("newpassword123");
        assertEquals("newpassword123", person.getPassword(), "Password should be updated to 'newpassword123'");
    }

    @Test
    void getDescription() {
        assertEquals("A software engineer", person.getDescription(), "Description should be 'A software engineer'");
    }

    @Test
    void setDescription() {
        person.setDescription("A senior software engineer");
        assertEquals("A senior software engineer", person.getDescription(), "Description should be updated to 'A senior software engineer'");
    }

    @Test
    void getAddress() {
        assertEquals(address, person.getAddress(), "Address should match the provided address");
    }

    @Test
    void setAddress() {
        AddressEntity newAddress = new AddressEntity("USA", "California", "90210", 2);
        person.setAddress(newAddress);
        assertEquals(newAddress, person.getAddress(), "Address should be updated to the new address");
    }

    @Test
    void getSkills() {
        assertTrue(person.getSkills().isEmpty(), "Skills should initially be empty");
    }

    @Test
    void setSkills() {
        person.setSkills(Arrays.asList(skill1, skill2));
        assertEquals(2, person.getSkills().size(), "Skills list should contain 2 elements");
        assertTrue(person.getSkills().contains(skill1), "Skills should contain 'Java'");
        assertTrue(person.getSkills().contains(skill2), "Skills should contain 'SQL'");
    }

    @Test
    void addSkills() {
        person.addSkills(skill1);
        assertEquals(1, person.getSkills().size(), "Skills list should contain 1 element after adding a skill");
        assertTrue(person.getSkills().contains(skill1), "Skills should contain 'Java' after adding");
    }

    @Test
    void testToString() {
        person.setSkills(Arrays.asList(skill1, skill2));
        String expected = "1#Nome: John Doe\n" +
                "Email: john.doe@example.com\n" +
                "Descrição: A software engineer\n" +
                "Endereço: \n" +
                "    Pais: Brazil\n" +
                "    Estado: São Paulo\n" +
                "    CEP: 01000-000\n" +
                "Competencias: [1-Java\n" +
                "    Descrição: Object-Oriented Programming\n" +
                "\t2-SQL\n" +
                "    Descrição: Database Management]";
        assertEquals(expected.trim(), person.toString().trim(), "toString output does not match the expected format");
    }
}
