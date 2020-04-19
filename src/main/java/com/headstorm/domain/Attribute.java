package com.headstorm.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.util.UUID;

@NodeEntity
public class Attribute extends Entity {

    @Property
    public String name;

    @Property
    public AttributeType attributeType;

    public static Attribute createNew(String name, AttributeType attributeType) {
        return new Attribute(UUID.randomUUID().toString(), name, attributeType);
    }

    private Attribute(String guid, String name, AttributeType attributeType) {
        this.guid = guid;
        this.name = name;
        this.attributeType = attributeType;
    }

    public Attribute() {

    }

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                ", attributeType=" + attributeType +
                '}';
    }
}
