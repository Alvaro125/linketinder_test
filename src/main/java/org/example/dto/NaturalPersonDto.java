package org.example.dto;

import lombok.Data;
import org.example.entity.NaturalPersonEntity;
import jakarta.validation.constraints.*;
import java.util.Objects;

@Data
public class NaturalPersonDto {
    @NotBlank(message = "O nome não pode estar vazio")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String name;

    @NotBlank(message = "O email não pode estar vazio")
    @Email(message = "O email deve ser válido")
    private String email;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String description;

    @NotNull(message = "O endereço não pode ser nulo")
    private AddressDto address;

    @NotBlank(message = "A senha não pode estar vazia")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    private String password;

    @NotBlank(message = "O CPF não pode estar vazio")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos")
    private String cpf;

    @NotNull(message = "A idade não pode ser nula")
    @Min(value = 0, message = "A idade deve ser maior ou igual a 0")
    @Max(value = 150, message = "A idade deve ser menor ou igual a 150")
    private Integer age;

    public NaturalPersonDto(String name, String email, String description, AddressDto address, String password, String cpf, Integer age) {
        this.name = name;
        this.email = email;
        this.description = description;
        this.address = address;
        this.password = password;
        this.cpf = cpf;
        this.age = age;
    }

    public NaturalPersonEntity toEntity() {
        return new NaturalPersonEntity(
                name,
                email,
                password,
                description,
                address != null ? address.toEntity() : null,
                cpf,
                age,
                0
        );
    }
}
