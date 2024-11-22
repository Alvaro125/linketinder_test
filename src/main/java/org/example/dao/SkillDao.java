package org.example.dao;

import org.example.entity.SkillEntity;

import java.util.List;

public interface SkillDao {
    public abstract List<SkillEntity> getAll();

    public abstract SkillEntity create(SkillEntity skill);

    public abstract List<SkillEntity> insertToPerson(SkillEntity skill, Integer id);

    public abstract SkillEntity getById(Integer id);

    public abstract List<SkillEntity> getByIdPerson(Integer id);

    public abstract void updateById(SkillEntity person);

    public abstract void deleteById(Integer id);

    public abstract void deleteByIdPerson(Integer idPerson);
}
