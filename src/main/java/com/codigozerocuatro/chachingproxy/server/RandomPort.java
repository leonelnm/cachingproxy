package com.codigozerocuatro.chachingproxy.server;

import java.io.IOException;
import java.net.ServerSocket;

public final class RandomPort {

    public static int getRandomPort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo asignar un puerto aleatorio", e);
        }
    }

}
