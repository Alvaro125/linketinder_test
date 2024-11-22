package org.example.dao;

import org.example.entity.LegalPersonEntity;

import java.util.List;

public interface LegalPersonDao extends LoginDao<LegalPersonEntity> {
    public abstract List<LegalPersonEntity> getAll();

    public abstract LegalPersonEntity create(LegalPersonEntity person);

    public abstract LegalPersonEntity getById(Integer id);

    public abstract void updateById(LegalPersonEntity person);

    public abstract void deleteById(Integer id);
}
