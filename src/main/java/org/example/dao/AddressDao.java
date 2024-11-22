package org.example.dao;

import org.example.entity.AddressEntity;

public interface AddressDao {
    public abstract AddressEntity create(AddressEntity address);

    public abstract AddressEntity getById(Integer id);

    public abstract void updateById(AddressEntity address);

    public abstract void deleteById(Integer id);
}
