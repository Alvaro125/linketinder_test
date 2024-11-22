package org.example.services.impl;

import org.example.dao.NaturalPersonDao;
import org.example.dto.LoginDto;
import org.example.entity.LegalPersonEntity;
import org.example.entity.NaturalPersonEntity;
import org.example.services.LoginService;

public class LoginServiceImpl implements LoginService {
    public LoginServiceImpl(NaturalPersonDao naturalPersonDao) {
        this.naturalPersonDao = naturalPersonDao;
    }

    @Override
    public NaturalPersonEntity loginNaturalPerson(LoginDto req) {
        return naturalPersonDao.loginPerson(req);
    }

    @Override
    public LegalPersonEntity loginLegalPerson(LoginDto req) {
        return null;
    }

    private NaturalPersonDao naturalPersonDao;
}
