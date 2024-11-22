package org.example.dao;

import org.example.entity.NaturalPersonEntity;

import java.util.List;

public interface NaturalPersonDao extends LoginDao<NaturalPersonEntity> {
    public abstract List<NaturalPersonEntity> getAll();

    public abstract NaturalPersonEntity create(NaturalPersonEntity person);

    public abstract NaturalPersonEntity getById(Integer id);

    public abstract void updateById(NaturalPersonEntity person);

    public abstract void deleteById(Integer id);
}
