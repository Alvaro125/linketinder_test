package org.example.dto;

import lombok.Data;
import org.example.entity.AddressEntity;

@Data
public class AddressDto {
    public AddressDto(String country, String state, String cep) {
        this.country = country;
        this.state = state;
        this.cep = cep;
    }

    public AddressEntity toEntity() {
        return new AddressEntity(country, state, cep);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    private String country;
    private String state;
    private String cep;
}
