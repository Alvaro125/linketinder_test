package org.example.entity;

import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

public class PersonEntity {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PersonEntity(String name, String email, String password, String description, AddressEntity address, Integer id, List<SkillEntity> skills) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.description = description;
        this.address = address;
        this.skills = skills;
    }

    public PersonEntity(String name, String email, String password, String description, AddressEntity address) {
        this.id = 0;
        this.name = name;
        this.email = email;
        this.password = password;
        this.description = description;
        this.address = address;
        this.skills = skills;
    }

    public PersonEntity(String name, String email, String password, String description, AddressEntity address, Integer id) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.description = description;
        this.address = address;
    }

    public PersonEntity() {
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

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSkills(List<SkillEntity> skills) {
        this.skills = skills;
    }

    public List<SkillEntity> getSkills() {
        return this.skills;
    }

    public void addSkills(SkillEntity skill) {
        this.skills.add(skill);
    }

    @Override
    public String toString() {
        return String.valueOf(getId()) + "#Nome: " + this.getName() + """
                
                Email:\s""" + this.getEmail() + """
                
                Descrição:\s""" + this.getDescription() + """
                
                Endereço:\s
                   \s""" + String.valueOf(this.getAddress()) + """
                
                Competencias: ["""  + this.getSkills().stream()
                .map(SkillEntity::toString) // Converte cada SkillEntity para String
                .reduce((skill1, skill2) -> skill1 + "\n\t" + skill2) // Junta com quebra de linha
                .orElse("") + "]";
    }

    @NotNull(message = "O ID não pode ser nulo")
    @Min(value = 0, message = "O ID deve ser maior ou igual a 0")
    private Integer id;

    @NotBlank(message = "O nome não pode estar vazio")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String name;

    @NotBlank(message = "O e-mail não pode estar vazio")
    @Email(message = "O e-mail deve ser válido")
    private String email;

    @NotBlank(message = "A senha não pode estar vazia")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String password;

    @NotBlank(message = "A descrição não pode estar vazia")
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String description;

    @NotNull(message = "O endereço não pode ser nulo")
    private AddressEntity address;

    private List<SkillEntity> skills = new ArrayList<>();
}
