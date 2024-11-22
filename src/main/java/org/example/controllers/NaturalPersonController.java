package org.example.controllers;

import com.sun.net.httpserver.HttpExchange;
import org.example.dto.NaturalPersonDto;
import org.example.entity.NaturalPersonEntity;
import org.example.services.NaturalPersonService;
import org.example.annotations.Get;
import org.example.annotations.Post;
import org.example.utils.ValidatorUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NaturalPersonController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(NaturalPersonController.class.getName());
    private final NaturalPersonService naturalPersonService;

    public NaturalPersonController(NaturalPersonService naturalPersonService) {
        this.naturalPersonService = naturalPersonService;
    }

    @Get("/candidato")
    public void handleListAllNaturalPeople(HttpExchange request) {
        try {
            List<NaturalPersonEntity> candidates = naturalPersonService.listAll();
            sendResponse(request, 200, toJson(candidates));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error listing natural persons", e);
            sendResponse(request, 500, "Internal Server Error");
        }
    }

    @Get("/candidato/{id}")
    public void handleOneNaturalPerson(HttpExchange request, Map<String, String> pathParams) {
        try {
            int id = Integer.parseInt(pathParams.get("id"));
            NaturalPersonEntity candidate = naturalPersonService.oneById(id);
            sendResponse(request, 200, toJson(candidate));
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid ID format", e);
            sendResponse(request, 400, "Bad Request: Invalid ID format");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching candidate", e);
            sendResponse(request, 500, "Internal Server Error");
        }
    }

    @Post("/candidato")
    public void handleCreateNaturalPerson(HttpExchange request) {
        try {
            // Parse and validate request body
            String requestBody = readRequestBody(request);
            NaturalPersonDto candidateRequest = gson.fromJson(requestBody, NaturalPersonDto.class);
            ValidatorUtil.validate(candidateRequest);

            // Process valid candidate
            NaturalPersonEntity candidate = naturalPersonService.addUser(candidateRequest.toEntity());
            sendResponse(request, 201, toJson(candidate));
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Validation error", e);
            sendResponse(request, 400, "Bad Request: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating candidate", e);
            sendResponse(request, 500, "Internal Server Error");
        }
    }

    private String readRequestBody(HttpExchange request) throws IOException {
        try (InputStream requestBody = request.getRequestBody()) {
            return new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
