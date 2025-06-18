package com.codigozerocuatro.chachingproxy.server;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerService {

    private final Map<Integer, Server> servers = new ConcurrentHashMap<>();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public Server startNewConnection(String url, Integer port) {
        int portToUse = (port != null) ? port : RandomPort.getRandomPort();
        Server server = new Server(portToUse, url, httpClient);
        server.start();
        servers.put(portToUse, server);

        return server;
    }

    public void getServerConnections() {
        System.out.printf("[INFO] Conexiones activas: %d%n", servers.size());
        servers.forEach((port, server) ->
                System.out.printf(" - Puerto: %d, URL: %s%n", port, server.getOrigin())
        );
    }

    public void stopServer(int port) {
        Server server = servers.get(port);
        if (server != null) {
            server.stop();
            servers.remove(port);
            System.out.println("Server on port " + port + " stopped.");
        } else {
            System.out.println("No server found on port " + port);
        }
    }

    public Server changeServerPort(int oldPort) {
        Server server = servers.get(oldPort);
        if (server != null) {
            server.stop();
            servers.remove(oldPort);
            int newPort = RandomPort.getRandomPort();
            Server newServer = new Server(newPort, server.getOrigin(), httpClient);
            newServer.start();
            servers.put(newServer.getPort(), newServer);
            System.out.println("Server port changed from " + oldPort + " to " + newServer.getPort());
            return newServer;
        } else {
            System.out.println("No server found on port " + oldPort);
        }

        return null;
    }

    public void stopAllServers() {
        servers.forEach((port, server) -> {
            server.stop();
            System.out.printf("[INFO] Servidor en puerto %d detenido.%n", port);
        });
        servers.clear();
        System.out.println("All servers stopped.");
    }


}
