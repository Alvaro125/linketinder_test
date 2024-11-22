package org.example.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SkillEntityTest {

    private SkillEntity skillEntity;

    @BeforeEach
    void setUp() {
        // Set up a sample SkillEntity before each test
        skillEntity = new SkillEntity("Programming", "Ability to write code", 1);
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test if necessary
        skillEntity = null;
    }

    @Test
    void getTitle() {
        assertEquals("Programming", skillEntity.getTitle(), "Title should be 'Programming'");
    }

    @Test
    void setTitle() {
        skillEntity.setTitle("Cooking");
        assertEquals("Cooking", skillEntity.getTitle(), "Title should be 'Cooking'");
    }

    @Test
    void getDescription() {
        assertEquals("Ability to write code", skillEntity.getDescription(), "Description should be 'Ability to write code'");
    }

    @Test
    void setDescription() {
        skillEntity.setDescription("Ability to cook");
        assertEquals("Ability to cook", skillEntity.getDescription(), "Description should be 'Ability to cook'");
    }

    @Test
    void getId() {
        assertEquals(1, skillEntity.getId(), "ID should be 1");
    }

    @Test
    void setId() {
        skillEntity.setId(2);
        assertEquals(2, skillEntity.getId(), "ID should be 2");
    }

    @Test
    void testToString() {
        String expected = "1-Programming\n" +
                "    Descrição: Ability to write code";
        assertEquals(expected, skillEntity.toString(), "toString output is not as expected");
    }
}
