package org.example.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import org.example.annotations.Get;
import org.example.annotations.Post;
import org.example.annotations.Put;
import org.example.annotations.Delete;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Controller {
    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());
    private static final String CORS_ALLOW_ORIGIN = "*";
    private static final String CORS_ALLOW_METHODS = "GET,POST,PUT,DELETE,OPTIONS";
    private static final String CORS_ALLOW_HEADERS = "*";

    protected final Gson gson;

    public Controller() {
        this.gson = new Gson();
    }

    public void handleRequest(HttpExchange request) throws IOException {
        // CORS Headers
        addCorsHeaders(request);

        // Handle preflight OPTIONS request
        if (request.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            sendOptionsResponse(request);
            return;
        }

        // Find the corresponding handler method using reflection
        Map<String, String> pathParams = new HashMap<>();
        Method handlerMethod = getHandlerMethod(request.getRequestMethod(), request.getRequestURI().getPath(), pathParams);

        if (handlerMethod != null) {
            try {
                // Prepare arguments for the method
                Object[] args = prepareMethodArguments(handlerMethod, pathParams, request);
                handlerMethod.invoke(this, args);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erro ao invocar o método", e);
                sendResponse(request, 500, "Erro interno: " + e.getMessage());
            }
        } else {
            sendResponse(request, 404, "Rota não encontrada");
        }
    }

    private void addCorsHeaders(HttpExchange request) {
        request.getResponseHeaders().add("Access-Control-Allow-Origin", CORS_ALLOW_ORIGIN);
        request.getResponseHeaders().add("Access-Control-Allow-Methods", CORS_ALLOW_METHODS);
        request.getResponseHeaders().add("Access-Control-Allow-Headers", CORS_ALLOW_HEADERS);
        request.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
    }

    private void sendOptionsResponse(HttpExchange request) throws IOException {
        request.sendResponseHeaders(200, -1);
    }

    private Method getHandlerMethod(String method, String uri, Map<String, String> pathParams) {
        for (Method m : getClass().getDeclaredMethods()) {
            if (methodMatches(m, method, uri, pathParams)) {
                return m;
            }
        }
        return null;
    }

    private boolean methodMatches(Method method, String httpMethod, String uri, Map<String, String> pathParams) {
        if (method.isAnnotationPresent(Get.class) && httpMethod.equalsIgnoreCase("GET")) {
            return matchUriPattern(method.getAnnotation(Get.class).value(), uri, pathParams);
        }
        if (method.isAnnotationPresent(Post.class) && httpMethod.equalsIgnoreCase("POST")) {
            return matchUriPattern(method.getAnnotation(Post.class).value(), uri, pathParams);
        }
        if (method.isAnnotationPresent(Put.class) && httpMethod.equalsIgnoreCase("PUT")) {
            return matchUriPattern(method.getAnnotation(Put.class).value(), uri, pathParams);
        }
        if (method.isAnnotationPresent(Delete.class) && httpMethod.equalsIgnoreCase("DELETE")) {
            return matchUriPattern(method.getAnnotation(Delete.class).value(), uri, pathParams);
        }
        return false;
    }

    private boolean matchUriPattern(String pattern, String uri, Map<String, String> pathParams) {
        String[] patternParts = pattern.split("/");
        String[] uriParts = uri.split("/");

        if (patternParts.length != uriParts.length) {
            return false;
        }

        for (int i = 0; i < patternParts.length; i++) {
            if (patternParts[i].startsWith("{") && patternParts[i].endsWith("}")) {
                String paramName = patternParts[i].substring(1, patternParts[i].length() - 1);
                pathParams.put(paramName, uriParts[i]);
            } else if (!patternParts[i].equals(uriParts[i])) {
                return false;
            }
        }
        return true;
    }

    private Object[] prepareMethodArguments(Method method, Map<String, String> pathParams, HttpExchange request) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            if (parameter.getType().equals(HttpExchange.class)) {
                args[i] = request;
            } else if (parameter.getType().equals(Map.class)) {
                args[i] = pathParams;
            } else if (parameter.getType().equals(String.class) && pathParams.containsKey(parameter.getName())) {
                args[i] = pathParams.get(parameter.getName());
            }
        }
        return args;
    }

    protected void sendResponse(HttpExchange request, int statusCode, String response) {
        try {
            request.getResponseHeaders().add("Content-Type", "application/json");
            byte[] responseBytes = response == null ? new byte[0] : response.getBytes(StandardCharsets.UTF_8);
            request.sendResponseHeaders(statusCode, responseBytes.length);

            try (OutputStream os = request.getResponseBody()) {
                os.write(responseBytes);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao enviar resposta", e);
        }
    }

    protected String toJson(Object object) {
        return gson == null ? "{}" : gson.toJson(object);
    }
}
