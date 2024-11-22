package org.example.services;

import org.example.dto.LoginDto;
import org.example.entity.LegalPersonEntity;
import org.example.entity.NaturalPersonEntity;

public interface LoginService {
    public abstract NaturalPersonEntity loginNaturalPerson(LoginDto req);

    public abstract LegalPersonEntity loginLegalPerson(LoginDto req);
}
