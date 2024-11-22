package org.example.dao;

import org.example.entity.PersonEntity;

public interface PersonDao {
    public abstract PersonEntity create(PersonEntity person);

    public abstract PersonEntity getById(Integer id);

    public abstract void deleteById(Integer id);

    public abstract void updateById(PersonEntity person);
}
