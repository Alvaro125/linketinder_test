package org.example.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JobEntityTest {

    @Test
    void testJobEntityInitialization() {
        // Configuração dos objetos dependentes
        AddressEntity address = new AddressEntity("Brazil", "MS", "79800-000");
        address.setId(1);

        LegalPersonEntity legalPerson = new LegalPersonEntity();
        legalPerson.setId(1);
        legalPerson.setName("Empresa Teste");
        legalPerson.setCnpj("12.345.678/0001-99");

        // Instância de JobEntity
        JobEntity job = new JobEntity("Desenvolvedor Java",
                "Desenvolver aplicações em Java",
                address,
                legalPerson,
                101);

        // Validação dos atributos
        assertEquals("Desenvolvedor Java", job.getName());
        assertEquals("Desenvolver aplicações em Java", job.getDescription());
        assertEquals(address, job.getLocal());
        assertEquals(legalPerson, job.getPerson());
        assertEquals(101, job.getId());
    }

    @Test
    void testSettersAndGetters() {
        // Configuração inicial
        AddressEntity address = new AddressEntity("Argentina", "BA", "C1002");
        address.setId(2);

        LegalPersonEntity legalPerson = new LegalPersonEntity();
        legalPerson.setId(2);
        legalPerson.setName("Empresa Argentina");
        legalPerson.setCnpj("98.765.432/0001-11");

        JobEntity job = new JobEntity(null, null, null, null, null);

        // Atualizando atributos com setters
        job.setName("Analista de Dados");
        job.setDescription("Analisar grandes volumes de dados");
        job.setLocal(address);
        job.setPerson(legalPerson);
        job.setId(202);

        // Validação dos atributos
        assertEquals("Analista de Dados", job.getName());
        assertEquals("Analisar grandes volumes de dados", job.getDescription());
        assertEquals(address, job.getLocal());
        assertEquals(legalPerson, job.getPerson());
        assertEquals(202, job.getId());
    }

    @Test
    void testToString() {
        // Configuração dos objetos dependentes
        AddressEntity address = new AddressEntity("Brazil", "SP", "01000-000");
        address.setId(3);

        LegalPersonEntity legalPerson = new LegalPersonEntity();
        legalPerson.setId(3);
        legalPerson.setName("Tech Solutions");
        legalPerson.setCnpj("99.123.456/0001-77");

        JobEntity job = new JobEntity("Gerente de Projetos",
                "Gerenciar projetos de tecnologia",
                address,
                legalPerson,
                303);

        // Validação do toString()
        String expectedString = "#303\n" +
                "Nome da Vaga: Gerente de Projetos\n" +
                "Descrição da Vaga: Gerenciar projetos de tecnologia\n" +
                "Empresa: Tech Solutions(CNPJ:99.123.456/0001-77)\n" +
                "Endereço:\n" +
                "    Pais: Brazil\n" +
                "    Estado: SP\n" +
                "    CEP: 01000-000";

        assertEquals(expectedString.trim(), job.toString().trim());
    }
}
