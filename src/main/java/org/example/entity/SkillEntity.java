package org.example.entity;

import jakarta.validation.constraints.*;

public class SkillEntity {

    @NotBlank(message = "O título não pode estar vazio")
    @Size(max = 100, message = "O título deve ter no máximo 100 caracteres")
    private String title;

    @NotBlank(message = "A descrição não pode estar vazia")
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String description;

    @NotNull(message = "O ID não pode ser nulo")
    @Min(value = 0, message = "O ID deve ser maior ou igual a 0")
    private Integer id;

    // Construtor com parâmetros
    public SkillEntity(String title, String description, Integer id) {
        this.title = title;
        this.description = description;
        this.id = id;
    }

    // Construtor com título e descrição (id será 0 por padrão)
    public SkillEntity(String title, String description) {
        this.title = title;
        this.description = description;
        this.id = 0;  // Valor padrão para id
    }

    // Construtor padrão
    public SkillEntity() {
        this.id = 0;
    }

    // Getters e Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(getId()) + "-" + getTitle() + """
                
                    Descrição:\s\
                """ + getDescription();
    }
}
