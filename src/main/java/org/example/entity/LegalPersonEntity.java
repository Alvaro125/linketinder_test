package org.example.entity;

import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

public class LegalPersonEntity extends PersonEntity {

    @NotBlank(message = "O CNPJ não pode estar vazio")
    @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter exatamente 14 dígitos")
    private String cnpj;

    public LegalPersonEntity(String nome, String email, String password, String description, AddressEntity address, String cnpj, Integer id, List<SkillEntity> skills) {
        super(nome, email, password, description, address, id, skills);
        this.cnpj = cnpj;
    }

    public LegalPersonEntity(String nome, String email, String password, String description, AddressEntity address, String cnpj, Integer id) {
        super(nome, email, password, description, address, id);
        this.cnpj = cnpj;
    }

    public LegalPersonEntity(String nome, String email, String password, String description, AddressEntity address, String cnpj) {
        super(nome, email, password, description, address);
        this.cnpj = cnpj;
    }

    public LegalPersonEntity() {
        super();
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Override
    public String toString() {
        return super.toString() + """
                
                CNPJ:\s""" + this.getCnpj();
    }
}
