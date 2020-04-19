package com.headstorm.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NodeEntity
public class ObjectInstance extends Entity {

    public String name;

    @Relationship(type = "INSTANCE_OF")
    public Template template;


    transient public Map<String, Object> attributeValues;

    public static ObjectInstance createNew(String name, Template template, Map<String, Object> attributeValues) {
        return new ObjectInstance(UUID.randomUUID().toString(), name, template, attributeValues);
    }

    private ObjectInstance(String guid, String name, Template template, Map<String, Object> attributeValues) {
        this.guid = guid;
        this.name = name;
        this.template = template;
        if (attributeValues == null) {
            this.attributeValues = new HashMap<>();
        } else {
            this.attributeValues = attributeValues;
        }
    }

    public ObjectInstance() {

    }

}
