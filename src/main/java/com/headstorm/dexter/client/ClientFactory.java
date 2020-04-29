package com.headstorm.dexter.client;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

public class ClientFactory {

    private static DexterClient clientInstance;

    public static DexterClient getClient() {
        if (clientInstance == null) {
            if (System.getenv("NEO4J_URI") != null) {
                clientInstance = getExternalClient();
            }  else {
                clientInstance = getEmbeddedClient();
            }
        }
        return clientInstance;
    }


    private static DexterClient getEmbeddedClient() {
        Neo4jSessionFactory.configureEmbeddedSessions();
        return Neo4jDexterClient.createNew(Neo4jSessionFactory.getInstance().getNeo4jSession());
    }

    private static DexterClient getExternalClient() {
        Neo4jSessionFactory.configureBasicAuthSession(System.getenv("NEO4J_URI"),
                System.getenv("NEO4J_USER"),
                System.getenv("NEO4J_PW"));
        return Neo4jDexterClient.createNew(Neo4jSessionFactory.getInstance().getNeo4jSession());
    }

    private static class Neo4jSessionFactory {

        private static SessionFactory sessionFactory;
        private static Neo4jSessionFactory factory = new Neo4jSessionFactory();

        public static void configureBasicAuthSession(String uri, String user, String password) {
            Configuration configuration = new Configuration.Builder()
                    .uri(uri)
                    .credentials(user, password)
                    .build();
            configureSessonFactory(configuration);
        }

        public static void configureEmbeddedSessions() {
            Configuration configuration = new Configuration.Builder().build();
            configureSessonFactory(configuration);
        }

        private static void configureSessonFactory(Configuration config) {
            sessionFactory = new SessionFactory(config, "com.headstorm");
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
}