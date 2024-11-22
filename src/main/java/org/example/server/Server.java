package org.example.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.example.controllers.*;
import org.example.factorys.ControllerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static final int PORT = 8081;

    private final LoginController loginController;
    private final NaturalPersonController naturalPersonController;

    public Server() {
        this.loginController = ControllerFactory.createLogin();
        this.naturalPersonController = ControllerFactory.createNaturalPerson();
    }

    public void init() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

            // Register routes dynamically
            registerRoute(server, "/candidato", naturalPersonController);
            registerRoute(server, "/login", loginController);

            // Configure server
            server.setExecutor(null); // Use default executor
            server.start();

            LOGGER.info("Servidor rodando na porta " + PORT);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao iniciar o servidor", e);
        }
    }

    private void registerRoute(HttpServer server, String path, Object controller) {
        server.createContext(path, exchange -> {
            try {
                Method handlerMethod = controller.getClass().getMethod("handleRequest", HttpExchange.class);
                handlerMethod.invoke(controller, exchange);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erro ao processar requisição", e);
            }
        });
    }

    // Optional: Main method to start the server
    public static void main(String[] args) {
        new Server().init();
    }
}
