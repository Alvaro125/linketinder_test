package org.example.dto;

import lombok.Data;
import org.example.entity.LegalPersonEntity;

@Data
public class LegalPersonDto {
    public LegalPersonDto(String name, String email, String description, AddressDto address, String password, String cnpj) {
        this.name = name;
        this.email = email;
        this.description = description;
        this.address = address;
        this.password = password;
        this.cnpj = cnpj;
    }

    public LegalPersonEntity toEntity() {
        return new LegalPersonEntity(name, email, password, description, address.toEntity(), cnpj, 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    private String name;
    private String email;
    private String description;
    private AddressDto address;
    private String password;
    private String cnpj;
}
