package org.example.ui;

import org.example.entity.AddressEntity;
import org.example.entity.LegalPersonEntity;
import org.example.entity.SkillEntity;
import org.example.factorys.ServiceFactory;
import org.example.services.LegalPersonService;
import org.example.services.SkillService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LegalPersonUI {
    private final LegalPersonService legalPersonService;
    private final SkillService skillService;
    private final SkillsUI skillsUI;

    public LegalPersonUI() {
        this.legalPersonService = ServiceFactory.createLegalPerson(); // Corrected method name
        this.skillService = ServiceFactory.createSkill();
        this.skillsUI = new SkillsUI();
    }

    public void read() {
        List<LegalPersonEntity> companies = legalPersonService.listAll();
        String companyList = companies.stream()
                .map(Object::toString)
                .reduce("", (a, b) -> a + "\n" + b);

        System.out.println("""
            
                        @@Lista de Empresas@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                       \s\
            """ + companyList + """
            
                        @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\
            """);
    }

    public void create(BufferedReader br) throws IOException {
        String name = readNonEmptyInput(br, "Nome da Empresa:");
        String email = readNonEmptyInput(br, "Email da Empresa:");
        String password = readNonEmptyInput(br, "Senha:");
        String description = readNonEmptyInput(br, "Descrição:");
        AddressEntity address = readAddress(br);
        List<SkillEntity> skills = readSkills(br);

        String cnpj = readNonEmptyInput(br, "CNPJ:");

        LegalPersonEntity newCompany = new LegalPersonEntity(
                name, email, password, description, address, cnpj, 0, skills
        );

        System.out.println("\n\n" + newCompany);
        legalPersonService.addUser(newCompany);
    }

    public void update(BufferedReader br) throws IOException {
        this.read();
        System.out.print("Informe o id da Empresa:");
        Integer id = Integer.parseInt(br.readLine());

        Optional<LegalPersonEntity> optionalPerson = Optional.ofNullable(legalPersonService.oneById(id));
        if (optionalPerson.isEmpty()) {
            System.out.println(id + " NÃO EXISTE");
            return;
        }

        LegalPersonEntity person = optionalPerson.get();
        person = updatePersonDetails(br, person);

        List<SkillEntity> skills = readSkills(br);
        if (!skills.isEmpty()) {
            person.setSkills(skills);
        }

        legalPersonService.updateById(person);
    }

    public void delete(BufferedReader br) throws IOException {
        this.read();
        System.out.print("Informe o id da Empresa:");
        Integer id = Integer.parseInt(br.readLine());

        Optional<LegalPersonEntity> optionalPerson = Optional.ofNullable(legalPersonService.oneById(id));
        if (optionalPerson.isEmpty()) {
            System.out.println(id + " NÃO EXISTE");
            return;
        }

        legalPersonService.deleteById(optionalPerson.get());
    }

    private String readNonEmptyInput(BufferedReader br, String prompt) throws IOException {
        String input;
        do {
            System.out.print(prompt + " ");
            input = br.readLine().trim();
        } while (input.isEmpty());
        return input;
    }

    private AddressEntity readAddress(BufferedReader br) throws IOException {
        String country = readNonEmptyInput(br, "Endereço:\n\tPais:");
        String state = readNonEmptyInput(br, "\tEstado:");
        String cep = readNonEmptyInput(br, "\tCEP:");
        return new AddressEntity(country, state, cep);
    }

    private List<SkillEntity> readSkills(BufferedReader br) throws IOException {
        List<SkillEntity> skills = new ArrayList<>();
        skillsUI.read();
        System.out.println("Caso não queira adicionar uma competência, aperte ENTER");

        while (true) {
            System.out.print("ID da competência (ou ENTER para sair): ");
            String idSkill = br.readLine().trim();

            if (idSkill.isEmpty()) {
                break;
            }

            try {
                Integer skillId = Integer.parseInt(idSkill);
                Optional<SkillEntity> skill = Optional.ofNullable(skillService.oneById(skillId));
                skill.ifPresent(skills::add);
            } catch (NumberFormatException e) {
                System.out.println("ID inválido. Tente novamente.");
            }
        }

        return skills;
    }

    private LegalPersonEntity updatePersonDetails(BufferedReader br, LegalPersonEntity person) throws IOException {
        person.setName(updateField(br, "Nome da Empresa", person.getName()));
        person.setEmail(updateField(br, "Email da Empresa", person.getEmail()));
        person.setPassword(updateField(br, "Senha", person.getPassword()));
        person.setDescription(updateField(br, "Descrição", person.getDescription()));

        AddressEntity address = person.getAddress();
        address.setCountry(updateField(br, "Endereço:\n\tPais", address.getCountry()));
        address.setState(updateField(br, "\tEstado", address.getState()));
        address.setCep(updateField(br, "\tCEP", address.getCep()));

        person.setCnpj(updateField(br, "CNPJ", person.getCnpj()));

        return person;
    }

    private String updateField(BufferedReader br, String prompt, String currentValue) throws IOException {
        System.out.printf("%s (%s): ", prompt, currentValue);
        String value = br.readLine().trim();
        return value.isEmpty() ? currentValue : value;
    }
}