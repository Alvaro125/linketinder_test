package org.example.services;

import org.example.entity.LegalPersonEntity;

import java.util.List;

public interface LegalPersonService{
    public abstract List<LegalPersonEntity> listAll();

    public abstract LegalPersonEntity oneById(Integer id);

    public abstract void updateById(LegalPersonEntity person);

    public abstract LegalPersonEntity addUser(LegalPersonEntity person);

    public abstract void deleteById(LegalPersonEntity person);
}
