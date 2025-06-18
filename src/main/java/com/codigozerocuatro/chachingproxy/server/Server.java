package com.codigozerocuatro.chachingproxy.server;

import io.helidon.http.HeaderName;
import io.helidon.http.HeaderNames;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final String HOST = "localhost";
    private static final HeaderName CACHE_HEADER = HeaderNames.create("X-Proxy-Cache");

    private final Map<String, CachedResponse> cache = new ConcurrentHashMap<>();
    private final int port;
    private final String origin;
    private final HttpClient client;
    private final WebServer server;

    public Server(int port, String origin, HttpClient client) {
        this.port = port;
        this.origin = origin;
        this.client = client;
        this.server = initServer();
    }

    private WebServer initServer() {
        return WebServer.builder()
                .host(HOST)
                .port(port)
                .routing(r -> r.any(this::handleRequest))
                .build();
    }

    private void handleRequest(ServerRequest req, ServerResponse res) {
        String fullPath = req.path().toString();
        String query = req.query().toMap().entrySet().stream()
                .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                .reduce((a, b) -> a + "&" + b)
                .map(q -> "?" + q)
                .orElse("");

        String fullUrl = origin + fullPath + query;
        System.out.println("Recibida: " + fullUrl);

        if (cache.containsKey(fullUrl)) {
            CachedResponse cached = cache.get(fullUrl);
            respondWithCache(res, cached);
            return;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            CachedResponse cached = new CachedResponse(response.statusCode(), response.headers().map(), response.body());
            cache.put(fullUrl, cached);
            respondWithResponse(res, cached, "MISS");
            System.out.println("Petición a origen y almacenado en cache");
        } catch (IOException | InterruptedException e) {
            res.status(502).send("Error al obtener datos del origen.");
            e.printStackTrace();
        }
    }

    private void respondWithCache(ServerResponse res, CachedResponse cached) {
        System.out.println("Devolviendo desde cache");
        respondWithResponse(res, cached, "HIT");
    }

    private void respondWithResponse(ServerResponse res, CachedResponse cached, String cacheStatus) {
        res.status(cached.statusCode);

        cached.headers.forEach((name, values) -> {
            if (isValidHttpHeader(name)) {
                HeaderName headerName = HeaderNames.create(name);
                values.forEach(value -> res.headers().add(headerName, value));
            }
        });

        res.headers().set(CACHE_HEADER, cacheStatus);
        res.send(cached.body);
    }

    private boolean isValidHttpHeader(String name) {
        return name != null &&
                !name.startsWith(":") &&
                name.matches("^[!#$%&'*+.^_`|~0-9a-zA-Z-]+$");
    }

    public void start() {
        server.start();
        System.out.println("Servidor iniciado en el puerto " + port);
    }

    public void stop() {
        server.stop();
        System.out.println("Servidor detenido en el puerto " + port);
    }

    public int getPort() {
        return port;
    }

    public String getOrigin() {
        return origin;
    }

    public HttpClient getClient() {
        return client;
    }
}
