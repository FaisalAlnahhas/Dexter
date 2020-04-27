package com.headstorm.dexter.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class SimpleHttpResponse {
    public final HashMap<String, String> headers = new HashMap<>();
    public Object body = null;
    public int statusCode = 200;

    private static ObjectMapper mapper = new ObjectMapper();

    public void applyToServletResponse(HttpServletResponse response) throws IOException  {
        for (String x : headers.keySet()) {
            response.addHeader(x, headers.get(x));
        }
        response.setStatus(statusCode);
        response.getWriter().write(mapper.writeValueAsString(body));
    }

    public SimpleHttpResponse(Object body) {
        this.body = body;
    }
}

