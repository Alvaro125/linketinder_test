package org.example.services.impl;

import org.example.dao.AddressDao;
import org.example.dao.LegalPersonDao;
import org.example.dao.PersonDao;
import org.example.entity.LegalPersonEntity;
import org.example.services.LegalPersonService;
import org.example.utils.ValidatorUtil;

import java.util.List;

public class LegalPersonServiceImpl implements LegalPersonService {
    public LegalPersonServiceImpl(LegalPersonDao legalPersonDao, AddressDao addressDao, PersonDao personDao) {
        this.legalPersonDao = legalPersonDao;
        this.addressDao = addressDao;
        this.personDao = personDao;
    }

    public List<LegalPersonEntity> listAll() {
        return legalPersonDao.getAll();
    }

    public LegalPersonEntity oneById(Integer id) {
        return legalPersonDao.getById(id);
    }

    public void updateById(LegalPersonEntity person) {
        addressDao.updateById(person.getAddress());
        personDao.updateById(person);
        legalPersonDao.updateById(person);
    }

    public LegalPersonEntity addUser(LegalPersonEntity person) {
        ValidatorUtil.validate(person);
        person.setAddress(addressDao.create(person.getAddress()));
        person.setId(personDao.create(person).getId());
        legalPersonDao.create(person);
        return person;
    }

    public void deleteById(LegalPersonEntity person) {
        legalPersonDao.deleteById(person.getId());
        personDao.deleteById(person.getId());
        addressDao.deleteById(person.getAddress().getId());
    }

    private LegalPersonDao legalPersonDao;
    private PersonDao personDao;
    private AddressDao addressDao;
}
