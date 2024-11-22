package org.example.entity;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class NaturalPersonEntityTest {

    @Test
    void testNaturalPersonEntityInitialization() {
        // Instanciando NaturalPersonEntity
        AddressEntity address = new AddressEntity("Rua 123", "SP", "98765-432");
        SkillEntity skill = new SkillEntity("Java","Code");
        NaturalPersonEntity naturalPerson = new NaturalPersonEntity(
                "Carlos Silva",
                "carlos@domain.com",
                "password123",
                "A software developer",
                address,
                "123.456.789-10",
                30,
                1,
                Arrays.asList(skill)
        );

        // Verificando os valores atribuídos
        assertEquals(1, naturalPerson.getId());
        assertEquals("Carlos Silva", naturalPerson.getName());
        assertEquals("carlos@domain.com", naturalPerson.getEmail());
        assertEquals("password123", naturalPerson.getPassword());
        assertEquals("A software developer", naturalPerson.getDescription());
        assertEquals(address, naturalPerson.getAddress());
        assertEquals("123.456.789-10", naturalPerson.getCpf());
        assertEquals(30, naturalPerson.getAge());
        assertEquals(1, naturalPerson.getSkills().size());
    }

    @Test
    void testSettersAndGetters() {
        // Instanciando NaturalPersonEntity
        AddressEntity address = new AddressEntity("Avenida Paulista", "SP", "01000-000");
        NaturalPersonEntity naturalPerson = new NaturalPersonEntity();

        // Usando setters
        naturalPerson.setName("Lucas Costa");
        naturalPerson.setEmail("lucas@domain.com");
        naturalPerson.setPassword("securepassword");
        naturalPerson.setDescription("System administrator");
        naturalPerson.setAddress(address);
        naturalPerson.setCpf("987.654.321-00");
        naturalPerson.setAge(35);

        // Verificando os valores com getters
        assertEquals("Lucas Costa", naturalPerson.getName());
        assertEquals("lucas@domain.com", naturalPerson.getEmail());
        assertEquals("securepassword", naturalPerson.getPassword());
        assertEquals("System administrator", naturalPerson.getDescription());
        assertEquals(address, naturalPerson.getAddress());
        assertEquals("987.654.321-00", naturalPerson.getCpf());
        assertEquals(35, naturalPerson.getAge());
    }

    @Test
    void testToString() {
        // Instanciando NaturalPersonEntity
        AddressEntity address = new AddressEntity("Rua das Flores", "MG", "12345-678");
        NaturalPersonEntity naturalPerson = new NaturalPersonEntity(
                "Ana Pereira",
                "ana@domain.com",
                "password321",
                "Data analyst",
                address,
                "321.654.987-00",
                28,
                2
        );

        // Validando o método toString()
        String expectedString = "2#Nome: Ana Pereira\n" +
                "Email: ana@domain.com\n" +
                "Descrição: Data analyst\n" +
                "Endereço: \n" +
                "    Pais: Rua das Flores\n" +
                "    Estado: MG\n" +
                "    CEP: 12345-678\n" +
                "Competencias: []\n" +
                "Idade: 28\n" +
                "CPF: 321.654.987-00";
        assertEquals(expectedString, naturalPerson.toString());
    }

    @Test
    void testDefaultConstructor() {
        // Testando o construtor padrão
        NaturalPersonEntity naturalPerson = new NaturalPersonEntity();

        assertNull(naturalPerson.getName());
        assertNull(naturalPerson.getEmail());
        assertNull(naturalPerson.getPassword());
        assertNull(naturalPerson.getDescription());
        assertNull(naturalPerson.getAddress());
        assertNull(naturalPerson.getCpf());
        assertNull(naturalPerson.getAge());
        assertNull(naturalPerson.getId());
    }
}
