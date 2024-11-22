package org.example.services;

import org.example.entity.SkillEntity;

import java.util.List;

public interface SkillService {
    public abstract void addSkill(SkillEntity skill);

    public abstract void addSkillByPerson(Integer idPerson, List<SkillEntity> skills);

    public abstract void deleteSkillByPerson(Integer idPerson);

    public abstract List<SkillEntity> listSkills();

    public abstract List<SkillEntity> listSkillsByPerson(Integer idPerson);

    public abstract SkillEntity oneById(Integer id);

    public abstract void updateById(SkillEntity skill);

    public abstract void deleteAllbyPerson(Integer id);

    public abstract void deleteById(Integer id);
}
