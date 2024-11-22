package org.example.factorys;

import org.example.dao.*;
import org.example.services.*;
import org.example.services.impl.*;

public class ServiceFactory {
    public static LoginService createLogin() {
        NaturalPersonDao naturalPersonDao = DaoFactory.createNaturalPerson();
        return new LoginServiceImpl(naturalPersonDao);
    }

    public static NaturalPersonService createNaturalPerson() {
        NaturalPersonDao naturalPersonDao = DaoFactory.createNaturalPerson();
        AddressDao addressDao = DaoFactory.createAddress();
        PersonDao personDao = DaoFactory.createPerson();
        return new NaturalPersonServiceImpl(naturalPersonDao, addressDao, personDao);
    }

    public static LegalPersonService createLegalPerson() {
        LegalPersonDao legalPersonDao = DaoFactory.createLegalPerson();
        AddressDao addressDao = DaoFactory.createAddress();
        PersonDao personDao = DaoFactory.createPerson();
        return new LegalPersonServiceImpl(legalPersonDao, addressDao, personDao);
    }

    public static SkillService createSkill() {
        SkillDao skillDao = DaoFactory.createSkill();
        return new SkillServiceImpl(skillDao);
    }

    public static JobsService createJob() {
        JobDao jobDao = DaoFactory.createJob();
        LegalPersonDao legalPersonDao = DaoFactory.createLegalPerson();
        AddressDao addressDao = DaoFactory.createAddress();
        return new JobsServiceImpl(jobDao, legalPersonDao, addressDao);
    }

}
