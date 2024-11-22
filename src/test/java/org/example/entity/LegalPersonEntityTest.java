package org.example.entity;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LegalPersonEntityTest {

    @Test
    void testLegalPersonEntityInitialization() {
        // Instanciando LegalPersonEntity
        AddressEntity address = new AddressEntity("Rua ABC", "SP", "12345-678");
        SkillEntity skill = new SkillEntity("Java","Language");
        LegalPersonEntity legalPerson = new LegalPersonEntity(
                "Tech Solutions",
                "contact@techsolutions.com",
                "password123",
                "A tech company",
                address,
                "99.123.456/0001-77",
                1,
                Arrays.asList(skill)
        );

        // Verificando os valores atribuídos
        assertEquals(1, legalPerson.getId());
        assertEquals("Tech Solutions", legalPerson.getName());
        assertEquals("contact@techsolutions.com", legalPerson.getEmail());
        assertEquals("password123", legalPerson.getPassword());
        assertEquals("A tech company", legalPerson.getDescription());
        assertEquals(address, legalPerson.getAddress());
        assertEquals("99.123.456/0001-77", legalPerson.getCnpj());
        assertEquals(1, legalPerson.getSkills().size());
    }

    @Test
    void testSettersAndGetters() {
        // Instanciando LegalPersonEntity
        AddressEntity address = new AddressEntity("Rua XYZ", "RJ", "98765-432");
        LegalPersonEntity legalPerson = new LegalPersonEntity();

        // Usando setters
        legalPerson.setName("SoftCorp");
        legalPerson.setEmail("info@softcorp.com");
        legalPerson.setPassword("securepassword");
        legalPerson.setDescription("Software Solutions");
        legalPerson.setAddress(address);
        legalPerson.setCnpj("22.334.445/0001-99");

        // Verificando os valores com getters
        assertEquals("SoftCorp", legalPerson.getName());
        assertEquals("info@softcorp.com", legalPerson.getEmail());
        assertEquals("securepassword", legalPerson.getPassword());
        assertEquals("Software Solutions", legalPerson.getDescription());
        assertEquals(address, legalPerson.getAddress());
        assertEquals("22.334.445/0001-99", legalPerson.getCnpj());
    }

    @Test
    void testToString() {
        // Instanciando LegalPersonEntity
        AddressEntity address = new AddressEntity("Avenida Paulista", "SP", "01000-000");
        LegalPersonEntity legalPerson = new LegalPersonEntity(
                "DataTech",
                "contact@datatech.com",
                "password1234",
                "Data science company",
                address,
                "11.223.344/0001-11",
                2
        );

        // Validando o método toString()
        String expectedString = "2#Nome: DataTech\n" +
                "Email: contact@datatech.com\n" +
                "Descrição: Data science company\n" +
                "Endereço: \n" +
                "    Pais: Avenida Paulista\n" +
                "    Estado: SP\n" +
                "    CEP: 01000-000\n" +
                "Competencias: []\n" +
                "CNPJ: 11.223.344/0001-11";
        assertEquals(expectedString, legalPerson.toString());
    }

    @Test
    void testDefaultConstructor() {
        // Testando o construtor padrão
        LegalPersonEntity legalPerson = new LegalPersonEntity();

        assertNull(legalPerson.getName());
        assertNull(legalPerson.getEmail());
        assertNull(legalPerson.getPassword());
        assertNull(legalPerson.getDescription());
        assertNull(legalPerson.getAddress());
        assertNull(legalPerson.getCnpj());
        assertNull(legalPerson.getId());
    }
}
