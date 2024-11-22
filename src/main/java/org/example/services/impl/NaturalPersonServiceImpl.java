package org.example.services.impl;

import org.example.dao.AddressDao;
import org.example.dao.NaturalPersonDao;
import org.example.dao.PersonDao;
import org.example.entity.NaturalPersonEntity;
import org.example.services.NaturalPersonService;

import java.util.List;

public class NaturalPersonServiceImpl implements NaturalPersonService {
    public NaturalPersonServiceImpl(NaturalPersonDao naturalPersonDao, AddressDao addressDao, PersonDao personDao) {
        this.naturalPersonDao = naturalPersonDao;
        this.addressDao = addressDao;
        this.personDao = personDao;
    }

    public List<NaturalPersonEntity> listAll() {
        return naturalPersonDao.getAll();
    }

    public NaturalPersonEntity oneById(Integer id) {
        return naturalPersonDao.getById(id);
    }

    public void updateById(NaturalPersonEntity person) {
        addressDao.updateById(person.getAddress());
        personDao.updateById(person);
        naturalPersonDao.updateById(person);
    }

    public NaturalPersonEntity addUser(NaturalPersonEntity person) {
        person.setAddress(addressDao.create(person.getAddress()));
        person.setId(personDao.create(person).getId());
        naturalPersonDao.create(person);
        return person;
    }

    public void deleteById(NaturalPersonEntity person) {
        naturalPersonDao.deleteById(person.getId());
        personDao.deleteById(person.getId());
        addressDao.deleteById(person.getAddress().getId());
    }

    private NaturalPersonDao naturalPersonDao;
    private PersonDao personDao;
    private AddressDao addressDao;
}
