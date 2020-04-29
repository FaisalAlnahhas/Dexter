package com.headstorm.dexter.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;
import java.util.UUID;

@NodeEntity
public class Tag extends Entity{

    @Relationship
    public List<Entity> taggedEntities;

    @Property
    public String name;

    public static Tag createNew(String name) {
        return new Tag(UUID.randomUUID().toString(), name);
    }

    public Tag() {}

    private Tag (String guid, String name) {
        this.guid = guid;
        this.name = name;
    }
}
