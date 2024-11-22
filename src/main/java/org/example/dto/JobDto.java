package org.example.dto;

import lombok.Data;
import org.example.entity.AddressEntity;
import org.example.entity.JobEntity;
import org.example.entity.LegalPersonEntity;

@Data
public class JobDto {
    public JobDto(String name, String description, AddressDto local, Integer person) {
        this.name = name;
        this.description = description;
        this.local = local;
        this.person = person;
    }

    public JobEntity toEntity() {
        LegalPersonEntity legalPerson = new LegalPersonEntity();
        legalPerson.setId(person);
        return new JobEntity(name, description, new AddressEntity(local.getCountry(), local.getState(), local.getCep()), legalPerson, 0);
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

    public AddressDto getLocal() {
        return local;
    }

    public void setLocal(AddressDto local) {
        this.local = local;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    private String name;
    private String description;
    private AddressDto local;
    private Integer person;
}
