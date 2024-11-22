package org.example.ui;

import org.example.entity.AddressEntity;
import org.example.entity.NaturalPersonEntity;
import org.example.entity.SkillEntity;
import org.example.factorys.ServiceFactory;
import org.example.services.NaturalPersonService;
import org.example.services.SkillService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NaturalPersonUI {
    private static final NaturalPersonService naturalPersonService = ServiceFactory.createNaturalPerson();
    private static final SkillService skillService = ServiceFactory.createSkill();

    public void read() {
        System.out.println("\n@@Lista de Candidatos@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println(naturalPersonService.listAll().stream()
                .map(NaturalPersonEntity::toString)
                .reduce((a, b) -> a + "\n" + b)
                .orElse(""));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }

    public void create(BufferedReader br) throws IOException {
        List<SkillEntity> skills = new ArrayList<>();

        String name = readNonEmptyInput(br, "Nome do Candidato: ");
        String email = readNonEmptyInput(br, "Email do Candidato: ");
        String password = readNonEmptyInput(br, "Senha: ");
        String description = readNonEmptyInput(br, "Descrição: ");
        AddressEntity address = readAddress(br);
        skills.addAll(readSkills(br));

        String cpf = readNonEmptyInput(br, "CPF: ");
        int age = readIntegerInput(br, "Idade: ");

        NaturalPersonEntity newCandidate = new NaturalPersonEntity(
                name, email, password, description, address, cpf, age, 0, skills
        );

        System.out.println("\n" + newCandidate);
        naturalPersonService.addUser(newCandidate);
    }

    public void update(BufferedReader br) throws IOException {
        this.read();
        Integer id = readIntegerInput(br, "Informe o ID do Candidato: ");

        NaturalPersonEntity person = naturalPersonService.oneById(id);
        if (person == null) {
            System.out.println(id + " NÃO EXISTE");
            return;
        }

        updatePersonDetails(br, person);
        List<SkillEntity> skills = readSkills(br);

        if (!skills.isEmpty()) {
            person.setSkills(skills);
        }

        naturalPersonService.updateById(person);
    }

    public void delete(BufferedReader br) throws IOException {
        this.read();
        Integer id = readIntegerInput(br, "Informe o ID do Candidato: ");

        NaturalPersonEntity person = naturalPersonService.oneById(id);
        if (person == null) {
            System.out.println(id + " NÃO EXISTE");
        } else {
            naturalPersonService.deleteById(person);
        }
    }

    private String readNonEmptyInput(BufferedReader br, String prompt) throws IOException {
        String input;
        do {
            System.out.print(prompt);
            input = br.readLine().trim();
        } while (input.isEmpty());
        return input;
    }

    private AddressEntity readAddress(BufferedReader br) throws IOException {
        System.out.println("Endereço:");
        String country = readNonEmptyInput(br, "\tPaís: ");
        String state = readNonEmptyInput(br, "\tEstado: ");
        String cep = readNonEmptyInput(br, "\tCEP: ");
        return new AddressEntity(country, state, cep);
    }

    private List<SkillEntity> readSkills(BufferedReader br) throws IOException {
        List<SkillEntity> skills = new ArrayList<>();
        new SkillsUI().read();
        System.out.println("Caso não queira adicionar uma competência, aperte ENTER");

        int count = 1;
        while (true) {
            System.out.printf("Competencia #%d: ", count);
            String idSkill = br.readLine().trim();

            if (idSkill.isEmpty()) {
                break;
            }

            try {
                SkillEntity skill = skillService.oneById(Integer.parseInt(idSkill));
                if (skill != null) {
                    skills.add(skill);
                    count++;
                } else {
                    System.out.println("ID de Competência Inválido");
                }
            } catch (NumberFormatException e) {
                System.out.println("ID de Competência Inválido");
            }
        }

        return skills;
    }

    private int readIntegerInput(BufferedReader br, String prompt) throws IOException {
        while (true) {
            String input = readNonEmptyInput(br, prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Por favor, insira um número válido.");
            }
        }
    }

    private void updatePersonDetails(BufferedReader br, NaturalPersonEntity person) throws IOException {
        person.setName(updateField(br, "Nome do Candidato", person.getName()));
        person.setEmail(updateField(br, "Email do Candidato", person.getEmail()));
        person.setPassword(updateField(br, "Senha", person.getPassword()));
        person.setDescription(updateField(br, "Descrição", person.getDescription()));

        // Atualizar endereço
        System.out.println("Endereço:");
        AddressEntity address = person.getAddress();
        address.setCountry(updateField(br, "\tPaís", address.getCountry()));
        address.setState(updateField(br, "\tEstado", address.getState()));
        address.setCep(updateField(br, "\tCEP", address.getCep()));

        person.setCpf(updateField(br, "CPF", person.getCpf()));

        String ageInput = updateField(br, "Idade", String.valueOf(person.getAge()));
        person.setAge(Integer.parseInt(ageInput));
    }

    private String updateField(BufferedReader br, String prompt, String currentValue) throws IOException {
        System.out.printf("%s (%s): ", prompt, currentValue);
        String newValue = br.readLine().trim();
        return newValue.isEmpty() ? currentValue : newValue;
    }
}