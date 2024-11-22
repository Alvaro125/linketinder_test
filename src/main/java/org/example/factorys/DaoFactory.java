package org.example.factorys;

import org.example.config.Database;
import org.example.dao.*;
import org.example.dao.impl.*;

import java.sql.Connection;

public class DaoFactory {
    public static NaturalPersonDao createNaturalPerson() {
        Connection conn = Database.conn;
        return new NaturalPersonDaoImpl(conn);
    }

    public static LegalPersonDao createLegalPerson() {
        Connection conn = Database.conn;
        return new LegalPersonDaoImpl(conn);
    }

    public static AddressDao createAddress() {
        Connection conn = Database.conn;
        return new AddressDaoImpl(conn);
    }

    public static PersonDao createPerson() {
        Connection conn = Database.conn;
        return new PersonDaoImpl(conn);
    }

    public static SkillDao createSkill() {
        Connection conn = Database.conn;
        return new SkillDaoImpl(conn);
    }

    public static JobDao createJob() {
        Connection conn = Database.conn;
        return new JobDaoImpl(conn);
    }

}
