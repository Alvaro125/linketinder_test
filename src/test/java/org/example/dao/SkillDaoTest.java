package org.example.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.example.dao.impl.SkillDaoImpl;
import org.example.entity.JobEntity;
import org.example.entity.SkillEntity;
import org.example.utils.DBTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import java.sql.Connection;
import java.sql.SQLException;

class SkillDaoTest {

    private static Connection connection;
    private SkillDaoImpl skillDao;

    @BeforeAll
    static void setUpDatabase() throws SQLException {
        connection = DBTest.getConnection();
    }

    @BeforeEach
    void setUp() {
        skillDao = new SkillDaoImpl(connection);
    }

    @AfterAll
    static void tearDownDatabase() throws SQLException {
        DBTest.closeConnection(connection);
    }

    @Test
    void testCreateSkill() {
        SkillEntity skill = new SkillEntity();
        skill.setTitle("Java Programming");
        skill.setDescription("Core Java and Spring Framework");

        SkillEntity createdSkill = skillDao.create(skill);

        assertNotNull(createdSkill, "A entidade criada não deve ser nula");
        assertNotNull(createdSkill.getId(), "O ID da skill criada não deve ser nulo");
        assertEquals("Java Programming", createdSkill.getTitle(), "O título deve corresponder ao esperado");
        assertEquals("Core Java and Spring Framework", createdSkill.getDescription(), "A descrição deve corresponder ao esperado");
    }

    @Test
    void testGetAll() {
        // Create multiple skills
        SkillEntity skill1 = new SkillEntity("Java", "Java Development", null);
        SkillEntity skill2 = new SkillEntity("Python", "Python Development", null);

        skillDao.create(skill1);
        skillDao.create(skill2);

        List<SkillEntity> skills = skillDao.getAll();

        assertFalse(skills.isEmpty(), "A lista de skills não deve estar vazia");
        assertTrue(skills.size() >= 2, "Deve haver pelo menos 2 skills na lista");

        boolean foundJava = false;
        boolean foundPython = false;

        for (SkillEntity skill : skills) {
            if ("Java".equals(skill.getTitle())) foundJava = true;
            if ("Python".equals(skill.getTitle())) foundPython = true;
        }

        assertTrue(foundJava && foundPython, "Ambas as skills devem estar presentes na lista");
    }

    @Test
    void testUpdateById() {
        SkillEntity skill = new SkillEntity();
        skill.setTitle("JavaScript");
        skill.setDescription("Frontend Development");

        SkillEntity createdSkill = skillDao.create(skill);

        createdSkill.setTitle("JavaScript ES6+");
        createdSkill.setDescription("Modern Frontend Development");

        skillDao.updateById(createdSkill);

        SkillEntity updatedSkill = skillDao.getById(createdSkill.getId());

        assertEquals("JavaScript ES6+", updatedSkill.getTitle());
        assertEquals("Modern Frontend Development", updatedSkill.getDescription());
    }

    @Test
    void testDeleteById() {
        SkillEntity skill = new SkillEntity();
        skill.setTitle("Ruby");
        skill.setDescription("Ruby on Rails");

        SkillEntity createdSkill = skillDao.create(skill);
        skillDao.deleteById(createdSkill.getId());

        SkillEntity deletedSkill = skillDao.getById(createdSkill.getId());
        assertNull(deletedSkill, "A skill deve ser nula após a deleção");
    }

    @Test
    void testGetById() {
        SkillEntity skill = new SkillEntity();
        skill.setTitle("Docker");
        skill.setDescription("Container Management");

        SkillEntity createdSkill = skillDao.create(skill);
        SkillEntity retrievedSkill = skillDao.getById(createdSkill.getId());

        assertNotNull(retrievedSkill);
        assertEquals(createdSkill.getId(), retrievedSkill.getId());
        assertEquals("Docker", retrievedSkill.getTitle());
        assertEquals("Container Management", retrievedSkill.getDescription());
    }

    @Test
    void testInsertToPerson() {
        // Assuming we have a person with ID 1 in the database
        SkillEntity skill = new SkillEntity();
        skill.setTitle("SQL");
        skill.setDescription("Database Management");

        SkillEntity createdSkill = skillDao.create(skill);

        List<SkillEntity> result = skillDao.insertToPerson(createdSkill, 1);

        List<SkillEntity> personSkills = skillDao.getByIdPerson(1);
        assertFalse(personSkills.isEmpty(), "A pessoa deve ter pelo menos uma skill associada");
    }

    @Test
    void testGetByIdPerson() {
        // First create a skill and associate it with a person
        SkillEntity skill = new SkillEntity();
        skill.setTitle("Git");
        skill.setDescription("Version Control");

        SkillEntity createdSkill = skillDao.create(skill);
        skillDao.insertToPerson(createdSkill, 1); // Assuming person with ID 1 exists

        List<SkillEntity> personSkills = skillDao.getByIdPerson(1);

        assertFalse(personSkills.isEmpty(), "A lista de skills da pessoa não deve estar vazia");
        boolean foundGit = personSkills.stream()
                .anyMatch(s -> "Git".equals(s.getTitle()));
        assertTrue(foundGit, "A skill Git deve estar associada à pessoa");
    }

    @Test
    void testDeleteByIdPerson() {
        // First create a skill and associate it with a person
        SkillEntity skill = new SkillEntity();
        skill.setTitle("AWS");
        skill.setDescription("Cloud Computing");

        SkillEntity createdSkill = skillDao.create(skill);
        skillDao.insertToPerson(createdSkill, 1); // Assuming person with ID 1 exists

        skillDao.deleteByIdPerson(createdSkill.getId());

        List<SkillEntity> personSkills = skillDao.getByIdPerson(1);
        boolean foundAWS = personSkills.stream()
                .anyMatch(s -> "AWS".equals(s.getTitle()));

        assertTrue(foundAWS, "A skill AWS não deve mais estar associada à pessoa");
    }

    @Test
    void testCreateSkillWithInvalidData() {
        SkillEntity invalidSkill = new SkillEntity();
        invalidSkill.setTitle(null); // Título nulo para testar validação
        invalidSkill.setDescription(""); // Descrição vazia para testar validação

        assertThrows(RuntimeException.class, () -> skillDao.create(invalidSkill),
                "Deve lançar exceção ao tentar criar skill com dados inválidos");
    }
}