package org.example.services.impl;

import org.example.dao.SkillDao;
import org.example.entity.SkillEntity;
import org.example.services.SkillService;
import org.example.utils.ValidatorUtil;

import java.util.List;

public class SkillServiceImpl implements SkillService {
    public SkillServiceImpl(SkillDao skillDao) {
        this.skillDao = skillDao;
    }

    public void addSkill(SkillEntity skill) {
        ValidatorUtil.validate(skill);
        skillDao.create(skill);
    }

    public void addSkillByPerson(Integer idPerson, List<SkillEntity> skills) {
        for (SkillEntity skill : skills) {
            skillDao.insertToPerson(skill, idPerson);
        }

    }

    public void deleteSkillByPerson(Integer idPerson) {
        skillDao.deleteByIdPerson(idPerson);
    }

    public List<SkillEntity> listSkills() {
        return skillDao.getAll();
    }

    public List<SkillEntity> listSkillsByPerson(Integer idPerson) {
        return skillDao.getByIdPerson(idPerson);
    }

    public SkillEntity oneById(Integer id) {
        return skillDao.getById(id);
    }

    public void updateById(SkillEntity skill) {
        try {
            SkillEntity skillResult = skillDao.getById(skill.getId());
            if (skillResult == null) {
                throw new RuntimeException("Id not Found");
            }
            skillDao.updateById(skill);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAllbyPerson(Integer id) {
        skillDao.deleteByIdPerson(id);
    }

    public void deleteById(Integer id) {
        try {
            SkillEntity skillResult = skillDao.getById(id);
            if (skillResult == null) {
                throw new RuntimeException("Id not Found");
            }
            this.deleteAllbyPerson(id);
            skillDao.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SkillDao skillDao;
}
