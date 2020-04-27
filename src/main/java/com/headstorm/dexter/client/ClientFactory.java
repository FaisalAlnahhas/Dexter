package com.headstorm.dexter.client;

public class ClientFactory {

    private static DexterClient clientInstance;

    public static DexterClient getClientInstance() {
        if (clientInstance == null) {
            clientInstance = Neo4jDexterClient.createNew(System.getenv("NEO4J_URI"),
                    System.getenv("NEO4J_USER"),
                    System.getenv("NEO4J_PW"));
        }
        return clientInstance;
    }
}
