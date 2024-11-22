package org.example.entity;

import java.util.ArrayList;
import java.util.List;

public class NaturalPersonEntity extends PersonEntity {
    public NaturalPersonEntity(String name, String email, String password, String description, AddressEntity address, String cpf, Integer age, Integer id, List<SkillEntity> skills) {
        super(name, email, password, description, address, id, skills);
        this.cpf = cpf;
        this.age = age;
        this.setAddress(address);
    }
    public NaturalPersonEntity(String name, String email, String password, String description, AddressEntity address, String cpf, Integer age, Integer id) {
        super(name, email, password, description, address, id, new ArrayList<SkillEntity>());
        this.cpf = cpf;
        this.age = age;
        this.setAddress(address);
    }

    public NaturalPersonEntity() {
        super();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return super.toString() + """
                
                Idade:\s""" + String.valueOf(this.getAge()) + """
                
                CPF:\s""" + this.getCpf();
    }

    private String cpf;
    private Integer age;
}
