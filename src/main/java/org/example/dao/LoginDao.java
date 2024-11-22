package org.example.dao;

import org.example.dto.LoginDto;

public interface LoginDao<T> {
    public abstract T loginPerson(LoginDto req);
}
