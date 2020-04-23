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

    @Property
    public AttributeType secondaryType;

    public static Attribute createNew(String name, AttributeType attributeType) {
        return Attribute.createNew(name, attributeType, null);
    }

    public static Attribute createNew(String name, AttributeType attributeType, AttributeType secondaryType) {
        return new Attribute(UUID.randomUUID().toString(), name, attributeType, secondaryType);
    }

    private Attribute(String guid, String name, AttributeType attributeType, AttributeType secondaryType) {
        this.guid = guid;
        this.name = name;
        this.attributeType = attributeType;
        this.secondaryType = secondaryType;
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
