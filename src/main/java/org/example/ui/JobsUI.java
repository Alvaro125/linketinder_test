package org.example.ui;

import org.example.entity.AddressEntity;
import org.example.entity.JobEntity;
import org.example.entity.LegalPersonEntity;
import org.example.entity.SkillEntity;
import org.example.factorys.ServiceFactory;
import org.example.services.JobsService;
import org.example.services.LegalPersonService;
import org.example.services.SkillService;

import java.io.BufferedReader;
import java.io.IOException;

public class JobsUI {
    private static final SkillService skillService = ServiceFactory.createSkill();
    private static final JobsService jobsService = ServiceFactory.createJob();
    private static final LegalPersonService legalPersonService = ServiceFactory.createLegalPerson();

    public void read() {
        System.out.println("\n@@Lista de Vagas@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println(jobsService.listAll().stream()
                .map(JobEntity::toString)
                .reduce((a, b) -> a + "\n" + b)
                .orElse(""));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }

    public void create(BufferedReader br) throws IOException {
        LegalPersonEntity person = getValidLegalPerson(br);
        if (person == null) {
            return;
        }

        String name = readNonEmptyInput(br, "Titulo do Emprego: ");
        String description = readNonEmptyInput(br, "Descrição do Emprego: ");
        AddressEntity address = getAddressFromInput(br);

        JobEntity job = new JobEntity(name, description, address, person, 0);
        jobsService.addJob(job);
        System.out.println("Vaga criada com sucesso!");
    }

    public void update(BufferedReader br) throws IOException {
        LegalPersonEntity person = getValidLegalPerson(br);
        if (person == null) {
            return;
        }

        System.out.println(jobsService.listByPerson(person.getId()).stream()
                .map(JobEntity::toString)
                .reduce((a, b) -> a + "\n" + b)
                .orElse(""));
        System.out.print("Vaga: ");

        JobEntity job = getValidJob(br, person);
        if (job == null) {
            return;
        }

        updateJobDetails(br, job);
        jobsService.updateById(job);
        System.out.println("Vaga atualizada com sucesso!");
    }

    public void delete(BufferedReader br) throws IOException {
        LegalPersonEntity person = getValidLegalPerson(br);
        if (person == null) {
            return;
        }

        System.out.println(jobsService.listByPerson(person.getId()).stream()
                .map(JobEntity::toString)
                .reduce((a, b) -> a + "\n" + b)
                .orElse(""));
        System.out.print("Vaga: ");

        JobEntity job = getValidJob(br, person);
        if (job == null) {
            return;
        }

        jobsService.deleteById(job);
        System.out.println("Vaga removida com sucesso!");
    }

    private JobEntity getValidJob(BufferedReader br, LegalPersonEntity person) throws IOException {
        while (true) {
            String idJob = br.readLine().trim();
            if (idJob.isEmpty()) {
                return null;
            }

            try {
                JobEntity job = jobsService.oneById(Integer.parseInt(idJob));
                if (job != null && job.getPerson().getId() == person.getId()) {
                    return job;
                }
                System.out.println("Id da Vaga Inválido");
            } catch (NumberFormatException e) {
                System.out.println("Id da Vaga Inválido");
            }
        }
    }

    private void updateJobDetails(BufferedReader br, JobEntity job) throws IOException {
        System.out.printf("Titulo do Emprego (%s): ", job.getName());
        String name = br.readLine().trim();
        if (!name.isEmpty()) {
            job.setName(name);
        }

        System.out.printf("Descrição do Emprego (%s): ", job.getDescription());
        String description = br.readLine().trim();
        if (!description.isEmpty()) {
            job.setDescription(description);
        }

        System.out.println("Endereço:");
        AddressEntity currentAddress = job.getLocal();

        System.out.printf("\tPaís (%s): ", currentAddress.getCountry());
        String country = br.readLine().trim();
        if (!country.isEmpty()) {
            currentAddress.setCountry(country);
        }

        System.out.printf("\tEstado (%s): ", currentAddress.getState());
        String state = br.readLine().trim();
        if (!state.isEmpty()) {
            currentAddress.setState(state);
        }

        System.out.printf("\tCEP (%s): ", currentAddress.getCep());
        String cep = br.readLine().trim();
        if (!cep.isEmpty()) {
            currentAddress.setCep(cep);
        }
    }

    private LegalPersonEntity getValidLegalPerson(BufferedReader br) throws IOException {
        new LegalPersonUI().read();
        while (true) {
            System.out.print("Empresa: ");
            String idPerson = br.readLine().trim();

            if (idPerson.isEmpty()) {
                return null;
            }

            try {
                LegalPersonEntity person = legalPersonService.oneById(Integer.parseInt(idPerson));
                if (person != null) {
                    return person;
                }
                System.out.println("Id da Empresa Inválido");
            } catch (NumberFormatException e) {
                System.out.println("Id da Empresa Inválido");
            }
        }
    }

    private AddressEntity getAddressFromInput(BufferedReader br) throws IOException {
        System.out.println("Endereço:");
        String country = readNonEmptyInput(br, "\tPaís: ");
        String state = readNonEmptyInput(br, "\tEstado: ");
        String cep = readNonEmptyInput(br, "\tCEP: ");
        return new AddressEntity(country, state, cep);
    }

    private String readNonEmptyInput(BufferedReader br, String prompt) throws IOException {
        String input;
        do {
            System.out.print(prompt);
            input = br.readLine().trim();

            if (input.isEmpty()) {
                System.out.println("Campo obrigatório, por favor, preencha.");
            }
        } while (input.isEmpty());

        return input;
    }
}