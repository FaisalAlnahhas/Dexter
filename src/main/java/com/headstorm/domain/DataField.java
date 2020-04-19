package com.headstorm.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import java.util.UUID;

@NodeEntity
public class DataField extends Entity {

    public String name;
    public DataType dataType;

    public static DataField createNew(String name, DataType dataType) {
        return new DataField(UUID.randomUUID().toString(), name, dataType);
    }

    private DataField(String guid, String name, DataType dataType) {
        this.guid = guid;
        this.name = name;
        this.dataType = dataType;
    }

    public DataField() {

    }

}
