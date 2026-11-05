package edu.upc.dsa;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);

    public static final String BASE_URI = "http://localhost:8080/dsa/";

    public static HttpServer startServer() {
        // busca los servicios REST en este paquete
        ResourceConfig rc = new ResourceConfig().packages("edu.upc.dsa.services");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = startServer();
        logger.info("Servidor arrancado en " + BASE_URI + " (pulsa ENTER para pararlo)");
        System.in.read();
        server.shutdownNow();
        logger.info("Servidor parado");
    }
}
