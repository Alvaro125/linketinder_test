package org.example.factorys;

import org.example.controllers.LoginController;
import org.example.controllers.NaturalPersonController;
import org.example.services.JobsService;
import org.example.services.LegalPersonService;
import org.example.services.LoginService;
import org.example.services.NaturalPersonService;

public class ControllerFactory {
    public static LoginController createLogin() {
        LoginService loginService = ServiceFactory.createLogin();
        return new LoginController(loginService);
    }

    public static NaturalPersonController createNaturalPerson() {
        NaturalPersonService naturalPersonService = ServiceFactory.createNaturalPerson();
        return new NaturalPersonController(naturalPersonService);
    }

}
