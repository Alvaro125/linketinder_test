package org.example.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NaturalPersonDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidNaturalPersonDto() {
        // Cria um DTO válido
        AddressDto address = new AddressDto("Brasil", "Acre", "11111000");
        NaturalPersonDto dto = new NaturalPersonDto(
                "João Silva",
                "joao.silva@example.com",
                "Descrição de teste",
                address,
                "senhaSegura123",
                "12345678901",
                30
        );

        // Valida o DTO
        Set<ConstraintViolation<NaturalPersonDto>> violations = validator.validate(dto);

        // Não deve haver violações
        assertTrue(violations.isEmpty(), "O DTO válido não deveria ter violações");
    }

    @Test
    void testInvalidNaturalPersonDto() {
        // Cria um DTO inválido
        AddressDto address = null; // Endereço nulo
        NaturalPersonDto dto = new NaturalPersonDto(
                "", // Nome vazio
                "email-invalido", // Email inválido
                "Descrição de teste que excede o limite de caracteres permitido. ".repeat(20), // Descrição muito longa
                address,
                "123", // Senha muito curta
                "123", // CPF inválido
                -5 // Idade inválida
        );

        // Valida o DTO
        Set<ConstraintViolation<NaturalPersonDto>> violations = validator.validate(dto);

        // Deve haver várias violações
        assertFalse(violations.isEmpty(), "O DTO inválido deveria ter violações");

        // Verifica mensagens de erro específicas
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O nome não pode estar vazio")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O email deve ser válido")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("A descrição deve ter no máximo")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O endereço não pode ser nulo")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("A senha deve ter pelo menos 8 caracteres")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O CPF deve conter exatamente 11 dígitos")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("A idade deve ser maior ou igual a 0")));
    }
}
