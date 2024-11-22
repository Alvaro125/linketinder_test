package org.example.ui;

import org.example.services.NaturalPersonService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Terminal {
    private static NaturalPersonService naturalPersonService;
    private static LegalPersonUI legalPersonUI;
    private static NaturalPersonUI naturalPersonUI;
    private static SkillsUI skillsUI;
    private static JobsUI jobsUI;

    public Terminal() {
        naturalPersonUI = new NaturalPersonUI();
        legalPersonUI = new LegalPersonUI();
        skillsUI = new SkillsUI();
        jobsUI = new JobsUI();
    }

    public void run() {
        String option = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (!"0".equals(option)) {
            try {
                printMenu();
                System.out.print("Opção: ");
                option = br.readLine();

                switch (option) {
                    case "0":
                        System.out.println("""
                            ░▄▀█░█▀▄░█▀▀░█░█░█▀
                            ░█▀█░█▄▀░██▄░█▄█░▄█
                            ░░░░░░░░░░░░░░░░░░░░
                        """);
                        return;
                    case "1":
                        legalPersonUI.read();
                        break;
                    case "2":
                        legalPersonUI.create(br);
                        break;
                    case "3":
                        legalPersonUI.update(br);
                        break;
                    case "4":
                        legalPersonUI.delete(br);
                        break;
                    case "5":
                        naturalPersonUI.read();
                        break;
                    case "6":
                        naturalPersonUI.create(br);
                        break;
                    case "7":
                        naturalPersonUI.update(br);
                        break;
                    case "8":
                        naturalPersonUI.delete(br);
                        break;
                    case "9":
                        skillsUI.read();
                        break;
                    case "10":
                        skillsUI.create(br);
                        break;
                    case "11":
                        skillsUI.update(br);
                        break;
                    case "12":
                        skillsUI.delete(br);
                        break;
                    case "13":
                        jobsUI.read();
                        break;
                    case "14":
                        jobsUI.create(br);
                        break;
                    case "15":
                        jobsUI.update(br);
                        break;
                    case "16":
                        jobsUI.delete(br);
                        break;
                    default:
                        System.out.println("""
                            ░█▀▀░█▀█░█▀█░█▀█░█▀█
                            ░██▄░█▀▄░█▀▄░█▄█░█▀▄
                            ░░░░░░░░░░░░░░░░░░░░
                        """);
                }
            } catch (IOException e) {
                System.err.println("Erro ao ler entrada: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("""
        #####################################
        #PAINEL                             #
        #####################################
        # 0-Sair                            #
        # 1-Listar Empresas                 #
        # 2-Adicionar Empresa               #
        # 3-Atualizar Empresa               #
        # 4-Deletar Empresa                 #
        # 5-Listar Candidatos               #
        # 6-Adicionar Candidato             #
        # 7-Atualizar Candidato             #
        # 8-Deletar Candidato               #
        # 9-Listar Competencias             #
        #10-Adicionar Competencia           #
        #11-Atualizar Competencia           #
        #12-Deletar Competencia             #
        #13-Listar Empregos                 #
        #14-Adicionar Emprego               #
        #15-Atualizar Emprego               #
        #16-Deletar Emprego                 #
        #####################################
        """);
    }

    // Getter and Setter methods remain the same
    public static NaturalPersonService getNaturalPersonService() {
        return naturalPersonService;
    }

    public static void setNaturalPersonService(NaturalPersonService naturalPersonService) {
        Terminal.naturalPersonService = naturalPersonService;
    }

    public static LegalPersonUI getLegalPersonUI() {
        return legalPersonUI;
    }

    public static void setLegalPersonUI(LegalPersonUI legalPersonUI) {
        Terminal.legalPersonUI = legalPersonUI;
    }

    public static NaturalPersonUI getNaturalPersonUI() {
        return naturalPersonUI;
    }

    public static void setNaturalPersonUI(NaturalPersonUI naturalPersonUI) {
        Terminal.naturalPersonUI = naturalPersonUI;
    }

    public static SkillsUI getSkillsUI() {
        return skillsUI;
    }

    public static void setSkillsUI(SkillsUI skillsUI) {
        Terminal.skillsUI = skillsUI;
    }

    public static JobsUI getJobsUI() {
        return jobsUI;
    }

    public static void setJobsUI(JobsUI jobsUI) {
        Terminal.jobsUI = jobsUI;
    }
}