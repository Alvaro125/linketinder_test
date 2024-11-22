package org.example.services;

import org.example.config.Database;
import org.example.dao.SkillDao;
import org.example.dao.impl.SkillDaoImpl;
import org.example.entity.SkillEntity;
import org.example.services.SkillService;
import org.example.services.impl.SkillServiceImpl;
import org.example.utils.DBTest;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SkillServiceTest {

    private Connection conn;
    private SkillDao skillDao;

    @BeforeAll
    void setupDatabase() throws SQLException {
        conn = DBTest.getConnection();
        skillDao = new SkillDaoImpl(conn);
    }

    @AfterAll
    void cleanup() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    @Test
    void shouldAddASkill() throws SQLException {
        SkillService skillService = new SkillServiceImpl(skillDao);

        SkillEntity newSkill = new SkillEntity("Groovy", "Programming language",9);
        skillService.addSkill(newSkill);
        List<SkillEntity> skills = skillService.listSkills();

        assertNotNull(skills);
        assertEquals(7, skills.size());
        SkillEntity lastSkill = skills.get(6);
        assertEquals("Groovy", lastSkill.getTitle());
        assertEquals("Programming language", lastSkill.getDescription());
    }

    @Test
    void shouldGetASkillById() throws SQLException {
        SkillService skillService = new SkillServiceImpl(skillDao);

        SkillEntity skill = skillService.oneById(1);

        assertNotNull(skill);
        assertEquals("Java", skill.getTitle());
        assertEquals("Programming language", skill.getDescription());
    }

    @Test
    void shouldUpdateSkillById() throws SQLException {
        SkillService skillService = new SkillServiceImpl(skillDao);

        SkillEntity skill = skillService.oneById(7);
        skill.setTitle("CSS");
        skill.setDescription("Language Cascading Style Sheet");

        skillService.updateById(skill);

        List<SkillEntity> skills = skillService.listSkills();
        assertEquals(7, skills.size());
        assertEquals("CSS", skills.get(5).getTitle());
        assertEquals("Language Cascading Style Sheet", skills.get(5).getDescription());
    }

    @Test
    void shouldAddSkillAndLinkToPerson() throws SQLException {
        SkillService skillService = new SkillServiceImpl(skillDao);

        SkillEntity newSkill = new SkillEntity("Groovy", "Programming language",9);
        skillService.addSkill(newSkill);
        SkillEntity skill = skillService.oneById(8);
        System.out.println(skill);
        skillService.addSkillByPerson(7, List.of(skill));
        List<SkillEntity> skillsByPerson = skillService.listSkillsByPerson(7);

        assertNotNull(skillsByPerson);
        assertEquals(1, skillsByPerson.size());
        SkillEntity lastSkill = skillsByPerson.get(0);
        assertEquals("Groovy", lastSkill.getTitle());
        assertEquals("Programming language", lastSkill.getDescription());
    }

    @Test
    void shouldDeleteSkillInLinksToPeople() throws SQLException {
        SkillService skillService = new SkillServiceImpl(skillDao);

        int idPerson = 1;
        int idSkill = 1;

        List<SkillEntity> skillsByPersonBefore = skillService.listSkillsByPerson(idPerson);
        assertEquals(2, skillsByPersonBefore.size());

        skillService.deleteAllbyPerson(idSkill);

        List<SkillEntity> skillsByPersonAfter = skillService.listSkillsByPerson(idPerson);
        assertEquals(0, skillsByPersonAfter.size());
    }

    @Test
    void shouldDeleteSkillAndItsLinksToPeople() throws SQLException {
        SkillService skillService = new SkillServiceImpl(skillDao);

        int idPerson = 2;
        int idSkill = 1;

        List<SkillEntity> skillsByPersonBefore = skillService.listSkillsByPerson(idPerson);
        List<SkillEntity> skillsBefore = skillService.listSkills();
        assertEquals(1, skillsByPersonBefore.size());
        assertEquals(7, skillsBefore.size());

        skillService.deleteById(idSkill);

        List<SkillEntity> skillsByPersonAfter = skillService.listSkillsByPerson(idPerson);
        List<SkillEntity> skillsAfter = skillService.listSkills();
        assertEquals(1, skillsByPersonAfter.size());
        assertEquals(6, skillsAfter.size());
    }

    @Test
    void shouldThrowExceptionWhenSkillNotFoundById() {
        SkillService skillService = new SkillServiceImpl(skillDao);

        assertNull(
            skillService.oneById(999) // ID que não existe
        );
    }

    @Test
    void shouldNotAddInvalidSkill() throws SQLException {
        SkillService skillService = new SkillServiceImpl(skillDao);

        // Testando skill com título nulo
        SkillEntity invalidSkill = new SkillEntity(null, "Programming language", 9);
        SkillEntity finalInvalidSkill = invalidSkill;
        assertThrows(IllegalArgumentException.class, () -> {
            skillService.addSkill(finalInvalidSkill);
        });

        // Testando skill com descrição nula
        invalidSkill = new SkillEntity("Groovy", "", 9);
        SkillEntity finalInvalidSkill1 = invalidSkill;
        assertThrows(IllegalArgumentException.class, () -> {
            skillService.addSkill(finalInvalidSkill1);
        });

        // Testando skill com título e descrição vazios
        invalidSkill = new SkillEntity("", "", 9);
        SkillEntity finalInvalidSkill2 = invalidSkill;
        assertThrows(IllegalArgumentException.class, () -> {
            skillService.addSkill(finalInvalidSkill2);
        });
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentSkill() throws SQLException {
        SkillService skillService = new SkillServiceImpl(skillDao);

        SkillEntity skill = new SkillEntity("NonExistent", "Non-existent description", 9);
        skill.setId(999); // ID que não existe

        assertThrows(RuntimeException.class, () -> {
            skillService.updateById(skill);
        });
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentSkill() throws SQLException {
        SkillService skillService = new SkillServiceImpl(skillDao);

        assertThrows(RuntimeException.class, () -> {
            skillService.deleteById(999); // ID que não existe
        });
    }

    @Test
    void shouldDeleteSkillsByPerson() throws SQLException {
        SkillService skillService = new SkillServiceImpl(skillDao);

        // Definindo o ID da pessoa
        int idPerson = 7;

        // Verificando as skills associadas à pessoa antes da exclusão
        List<SkillEntity> skillsByPersonBefore = skillService.listSkillsByPerson(idPerson);
        assertEquals(2, skillsByPersonBefore.size());  // Ajuste conforme o número esperado de skills associadas à pessoa

        // Chamando o método para deletar as skills associadas à pessoa
        skillService.deleteSkillByPerson(idPerson);

        // Verificando as skills associadas à pessoa após a exclusão
        List<SkillEntity> skillsByPersonAfter = skillService.listSkillsByPerson(idPerson);
        assertEquals(0, skillsByPersonAfter.size());  // Espera-se que a lista de skills esteja vazia
    }
}
