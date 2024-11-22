package org.example.controllers;

import com.sun.net.httpserver.HttpExchange;
import org.example.dto.LoginDto;
import org.example.entity.NaturalPersonEntity;
import org.example.services.LoginService;
import org.example.annotations.Post;

public class LoginController extends Controller {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Post("/login/candidato")
    public void handleLoginCandidato(HttpExchange request) {
        try {
            String requestBody = new String(request.getRequestBody().readAllBytes());
            LoginDto loginRequest = gson.fromJson(requestBody, LoginDto.class);
            NaturalPersonEntity candidato = loginService.loginNaturalPerson(loginRequest);
            String response = gson.toJson(candidato);
            sendResponse(request, 200, response);
        } catch (Exception e) {
            sendResponse(request, 401, e.getMessage());
        }
    }

    // A implementação para a rota "/login/empresa" pode ser similar:
    // @Post("/login/empresa")
    // public void handleLoginEmpresa(HttpExchange request) { ... }
}
