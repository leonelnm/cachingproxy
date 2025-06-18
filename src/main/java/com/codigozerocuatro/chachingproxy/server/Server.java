package com.codigozerocuatro.chachingproxy.server;

import io.undertow.Undertow;
import io.undertow.util.Headers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final String HOST = "localhost";
    private static final Map<String, String> cache = new ConcurrentHashMap<>();

    public Undertow initServer(int port, String origin, HttpClient client) {

        return Undertow.builder()
                .addHttpListener(port, HOST)
                .setHandler(exchange -> {
                    String path = exchange.getRequestURI();
                    String query = exchange.getQueryString();
                    String fullPath = path + (query.isEmpty() ? "" : "?" + query);
                    String fullUrl = origin + fullPath;

                    System.out.println("Recibida: " + fullPath);

                    // Si está en caché
                    if (cache.containsKey(fullUrl)) {
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                        exchange.getResponseSender().send(cache.get(fullUrl));
                        System.out.println("Devolviendo desde cache");
                        return;
                    }

                    // Si no está en caché
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(fullUrl))
                            .GET()
                            .build();

                    try {
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        String body = response.body();
                        cache.put(fullUrl, body);
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                        exchange.getResponseSender().send(body);
                        System.out.println("Petición a origen y almacenado en cache");
                    } catch (IOException | InterruptedException e) {
                        exchange.setStatusCode(502);
                        exchange.getResponseSender().send("Error al obtener datos del origen.");
                    }
                })
                .build();
    }

}
