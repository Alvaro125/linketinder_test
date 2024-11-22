package org.example.services;

import org.example.entity.NaturalPersonEntity;

import java.util.List;

public interface NaturalPersonService {
    public abstract List<NaturalPersonEntity> listAll();

    public abstract NaturalPersonEntity oneById(Integer id);

    public abstract void updateById(NaturalPersonEntity person);

    public abstract NaturalPersonEntity addUser(NaturalPersonEntity person);

    public abstract void deleteById(NaturalPersonEntity person);
}
