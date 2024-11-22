package org.example.entity;

public class JobEntity {
    public JobEntity(String name, String description, AddressEntity local, LegalPersonEntity person, Integer id) {
        this.name = name;
        this.description = description;
        this.local = local;
        this.person = person;
        this.id = id;
    }
    public JobEntity() {}

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

    private String name;
    private String description;
    private AddressEntity local;
    private LegalPersonEntity person;
    private Integer id;
}
