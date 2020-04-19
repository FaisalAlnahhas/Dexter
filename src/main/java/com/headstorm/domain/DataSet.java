package com.headstorm.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import java.util.UUID;

@NodeEntity
public class DataSet extends Entity {

    public String name;

    public static DataSet createNew(String name) {
        return new DataSet(UUID.randomUUID().toString(), name);
    }

    private DataSet(String guid, String name) {
        this.guid = guid;
        this.name = name;
    }

    public DataSet() {

    }

}
