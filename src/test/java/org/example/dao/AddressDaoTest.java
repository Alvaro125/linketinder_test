package org.example.dao;
import org.example.dao.impl.AddressDaoImpl;
import org.example.entity.AddressEntity;
import org.example.utils.DBTest;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AddressDaoTest {

    private static Connection connection;
    private AddressDaoImpl addressDao;

    @BeforeAll
    static void setUpDatabase() throws SQLException {
        connection = DBTest.getConnection();
    }

    @BeforeEach
    void setUp() {
        addressDao = new AddressDaoImpl(connection);
    }

    @AfterAll
    static void tearDownDatabase() throws SQLException {
        DBTest.closeConnection(connection);
    }

    @Test
    void testCreateAddress() {
        AddressEntity address = new AddressEntity();
        address.setCountry("Test Country");
        address.setState("Test State");
        address.setCep("12345678");

        AddressEntity createdAddress = addressDao.create(address);

        assertNotNull(createdAddress, "A entidade criada não deve ser nula");
        assertNotNull(createdAddress.getId(), "O ID do endereço criado não deve ser nulo");
        assertEquals("Test Country", createdAddress.getCountry(), "O país deve corresponder ao esperado");
        assertEquals("Test State", createdAddress.getState(), "O estado deve corresponder ao esperado");
        assertEquals("12345678", createdAddress.getCep(), "O CEP deve corresponder ao esperado");
    }

    @Test
    void testCreate() {
        AddressEntity address = new AddressEntity();
        address.setCountry("Brazil");
        address.setState("Mato Grosso do Sul");
        address.setCep("79800000");

        AddressEntity createdAddress = addressDao.create(address);

        assertNotNull(createdAddress.getId());
        assertEquals("Brazil", createdAddress.getCountry());
        assertEquals("Mato Grosso do Sul", createdAddress.getState());
        assertEquals("79800000", createdAddress.getCep());
    }

    @Test
    void testUpdateById() {
        AddressEntity address = new AddressEntity();
        address.setCountry("Brazil");
        address.setState("Rio de Janeiro");
        address.setCep("20000000");

        AddressEntity createdAddress = addressDao.create(address);

        createdAddress.setState("Minas Gerais");
        createdAddress.setCep("30000000");

        addressDao.updateById(createdAddress);
        AddressEntity updatedAddress = addressDao.getById(createdAddress.getId());

        assertEquals("Minas Gerais", updatedAddress.getState());
        assertEquals("30000000", updatedAddress.getCep());
    }

    @Test
    void testDeleteById() {
        AddressEntity address = new AddressEntity();
        address.setCountry("Brazil");
        address.setState("Paraná");
        address.setCep("80000000");

        AddressEntity createdAddress = addressDao.create(address);
        addressDao.deleteById(createdAddress.getId());

        AddressEntity deletedAddress = addressDao.getById(createdAddress.getId());
        assertNull(deletedAddress);
    }

    @Test
    void testCreateAddressWithInvalidData() {
        AddressEntity invalidAddress = new AddressEntity();
        invalidAddress.setCountry(null); // País nulo para testar validação
        invalidAddress.setState("State");
        invalidAddress.setCep("1234567"); // CEP inválido (menos de 8 caracteres)

        // Esperamos que ocorra um erro durante a execução
        assertThrows(RuntimeException.class, () -> addressDao.create(invalidAddress));
    }
}
