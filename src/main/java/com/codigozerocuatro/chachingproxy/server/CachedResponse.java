package com.codigozerocuatro.chachingproxy.server;

import java.util.List;
import java.util.Map;

public class CachedResponse {
    public final int statusCode;
    public final Map<String, List<String>> headers;
    public final String body;

    public CachedResponse(int statusCode, Map<String, List<String>> headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }
}

