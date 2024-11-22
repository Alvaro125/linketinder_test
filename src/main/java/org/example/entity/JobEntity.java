package org.example.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class JobEntity {
    public JobEntity(String name, String description, AddressEntity local, LegalPersonEntity person, Integer id) {
        this.name = name;
        this.description = description;
        this.local = local;
        this.person = person;
        this.id = id;
    }
    public JobEntity(String name, String description, AddressEntity local, LegalPersonEntity person) {
        this.name = name;
        this.description = description;
        this.local = local;
        this.person = person;
        this.id = 0;
    }
    public JobEntity() {
        this.id = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AddressEntity getLocal() {
        return local;
    }

    public void setLocal(AddressEntity local) {
        this.local = local;
    }

    public LegalPersonEntity getPerson() {
        return person;
    }

    public void setPerson(LegalPersonEntity person) {
        this.person = person;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "#" + String.valueOf(getId()) + """
                
                Nome da Vaga:\s""" + getName() + """
                
                Descrição da Vaga:\s""" + getDescription() + """
                
                Empresa:\s""" + getPerson().getName() + "(CNPJ:" + getPerson().getCnpj() + """
                )
                Endereço:
                   \s""" + String.valueOf(getLocal()) + """
                
                """;
    }

    @NotBlank(message = "O nome da vaga não pode estar vazio")
    @Size(max = 100, message = "O nome da vaga deve ter no máximo 100 caracteres")
    private String name;

    @NotBlank(message = "A descrição da vaga não pode estar vazia")
    @Size(max = 500, message = "A descrição da vaga deve ter no máximo 500 caracteres")
    private String description;

    @NotNull(message = "O endereço não pode ser nulo")
    private AddressEntity local;

    @NotNull(message = "A empresa (pessoa jurídica) não pode ser nula")
    private LegalPersonEntity person;

    @NotNull(message = "O ID da vaga não pode ser nulo")
    private Integer id;
}
