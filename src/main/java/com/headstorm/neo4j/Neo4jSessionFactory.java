package com.headstorm.neo4j;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

public class Neo4jSessionFactory {

    private static SessionFactory sessionFactory;
    private static Neo4jSessionFactory factory = new Neo4jSessionFactory();

    public static void configureBasicAuthSession(String uri, String user, String password) {
        Configuration configuration = new Configuration.Builder().uri("bolt://localhost")
                .credentials("neo4j", "password")
                .build();

         sessionFactory = new SessionFactory(configuration, "com.headstorm");
    }

    public static Neo4jSessionFactory getInstance() {
        return factory;
    }

    // prevent external instantiation
    private Neo4jSessionFactory() {
    }

    public Session getNeo4jSession() {
        return sessionFactory.openSession();
    }
}

