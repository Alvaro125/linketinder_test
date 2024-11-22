package org.example.entity;

public class AddressEntity {
    public AddressEntity(String country, String state, String cep, Integer id) {
        this.country = country;
        this.state = state;
        this.cep = cep;
        this.id = id;
    }
    public AddressEntity(String country, String state, String cep) {
        this.country = country;
        this.state = state;
        this.cep = cep;
        this.id = 0;
    }

    public AddressEntity() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Pais: " + this.getCountry() + """
                
                    Estado:\s\
                """ + this.getState() + """
                
                    CEP:\s\
                """ + this.getCep();
    }

    private Integer id;
    private String country;
    private String state;
    private String cep;
}
