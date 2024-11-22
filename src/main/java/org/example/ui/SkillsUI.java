package org.example.ui;

import org.example.entity.SkillEntity;
import org.example.factorys.ServiceFactory;
import org.example.services.SkillService;

import java.io.BufferedReader;
import java.io.IOException;

public class SkillsUI {
    private static final SkillService skillService = ServiceFactory.createSkill();
    public void read() {
        System.out.println("\n@@Lista de Competencias@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println(skillService.listSkills().stream()
                .map(SkillEntity::toString)
                .reduce((a, b) -> a + "\n" + b)
                .orElse(""));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }

    public void create(BufferedReader br) throws IOException {
        SkillEntity skill = new SkillEntity();

        skill.setTitle(readInput(br, "Titulo da Competencia"));
        skill.setDescription(readInput(br, "Descrição da Competencia"));

        skillService.addSkill(skill);
        System.out.println("Competencia adicionada com sucesso!");
    }

    public void update(BufferedReader br) throws IOException {
        this.read();

        Integer id = readIntegerInput(br, "Informe o ID da Skill");
        SkillEntity skill = skillService.oneById(id);

        if (skill == null) {
            System.out.println(id + " NÃO EXISTE");
            return;
        }

        updateSkillDetails(br, skill);
        skillService.updateById(skill);
        System.out.println("Competencia atualizada com sucesso!");
    }

    public void delete(BufferedReader br) throws IOException {
        this.read();

        Integer id = readIntegerInput(br, "Informe o ID da Skill");
        SkillEntity skill = skillService.oneById(id);

        if (skill == null) {
            System.out.println(id + " NÃO EXISTE");
            return;
        }

        skillService.deleteById(id);
        System.out.println("Competencia removida com sucesso!");
    }

    private String readInput(BufferedReader br, String prompt) throws IOException {
        String input;
        do {
            System.out.print(prompt + ": ");
            input = br.readLine().trim();

            if (input.isEmpty()) {
                System.out.println("Campo obrigatório, por favor, preencha.");
            }
        } while (input.isEmpty());

        return input;
    }

    private Integer readIntegerInput(BufferedReader br, String prompt) throws IOException {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                return Integer.parseInt(br.readLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, por favor, insira um número inteiro.");
            }
        }
    }

    private void updateSkillDetails(BufferedReader br, SkillEntity skill) throws IOException {
        System.out.printf("Titulo da Competencia (%s): ", skill.getTitle());
        String title = br.readLine().trim();
        if (!title.isEmpty()) {
            skill.setTitle(title);
        }

        System.out.printf("Descrição da Competencia (%s): ", skill.getDescription());
        String description = br.readLine().trim();
        if (!description.isEmpty()) {
            skill.setDescription(description);
        }
    }
}