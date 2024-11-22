package org.example.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressEntityTest {

    private AddressEntity addressEntity;

    @BeforeEach
    void setUp() {
        // Initialize a test object before each test
        addressEntity = new AddressEntity("Brazil", "Mato Grosso do Sul", "79800-000", 1);
    }

    @AfterEach
    void tearDown() {
        // Cleanup after each test
        addressEntity = null;
    }

    @Test
    void getId() {
        assertEquals(1, addressEntity.getId(), "ID should be 1");
    }

    @Test
    void setId() {
        addressEntity.setId(2);
        assertEquals(2, addressEntity.getId(), "ID should be updated to 2");
    }

    @Test
    void getCountry() {
        assertEquals("Brazil", addressEntity.getCountry(), "Country should be 'Brazil'");
    }

    @Test
    void setCountry() {
        addressEntity.setCountry("Argentina");
        assertEquals("Argentina", addressEntity.getCountry(), "Country should be updated to 'Argentina'");
    }

    @Test
    void getState() {
        assertEquals("Mato Grosso do Sul", addressEntity.getState(), "State should be 'Mato Grosso do Sul'");
    }

    @Test
    void setState() {
        addressEntity.setState("São Paulo");
        assertEquals("São Paulo", addressEntity.getState(), "State should be updated to 'São Paulo'");
    }

    @Test
    void getCep() {
        assertEquals("79800-000", addressEntity.getCep(), "CEP should be '79800-000'");
    }

    @Test
    void setCep() {
        addressEntity.setCep("01000-000");
        assertEquals("01000-000", addressEntity.getCep(), "CEP should be updated to '01000-000'");
    }

    @Test
    void testToString() {
        String expected = "Pais: Brazil\n" +
                "    Estado: Mato Grosso do Sul\n" +
                "    CEP: 79800-000";
        assertEquals(expected, addressEntity.toString(), "toString output does not match expected format");
    }
}
