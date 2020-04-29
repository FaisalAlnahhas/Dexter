package com.headstorm.dexter.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class JettyServer implements AutoCloseable{
    private static final Server server;
    private static final ServletContextHandler rootContext;
    private static final Set<HttpServlet> servlets = new HashSet<>();
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        server = new Server();
        rootContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        rootContext.setContextPath("/");
        server.setHandler(rootContext);
    }

    public void start(int port) throws Exception {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);

        server.setConnectors(new Connector[] { connector});
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public void join() throws InterruptedException {
        server.join();
    }

    public void addServlet(String path, HttpServlet servlet) {
        ServletHolder holder = new ServletHolder(servlet);
        rootContext.addServlet(holder, path);
        servlets.add(servlet);
    }

    @Override
    public void close() throws Exception {
        stop();
    }

    public void addHandler(String path, String verb, Function<HttpServletRequest, SimpleHttpResponse> func) {
        HttpServlet servlet = createServlet(verb, func);
        ServletHolder holder = new ServletHolder(servlet);
        rootContext.addServlet(holder, path);
        servlets.add(servlet);
    }

    public static Map<String, Object> readRequestBody(HttpServletRequest req) throws IOException {
//        req.getPathTranslated()
        return mapper.readValue(req.getInputStream(), Map.class);
    }

    private HttpServlet createServlet(String verb, Function<HttpServletRequest, SimpleHttpResponse> func) {
        if (verb.equals("GET")) {
            return createGetHandler(func);
        } else {
            return createPostHandler(func);
        }

    }

    private HttpServlet createGetHandler(Function<HttpServletRequest, SimpleHttpResponse> func) {
        return new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                SimpleHttpResponse response = func.apply(req);
                response.applyToServletResponse(resp);
            }
        };
    }

    private HttpServlet createPostHandler(Function<HttpServletRequest, SimpleHttpResponse> func) {
        return new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                SimpleHttpResponse response = func.apply(req);
                response.applyToServletResponse(resp);
            }
        };
    }
}