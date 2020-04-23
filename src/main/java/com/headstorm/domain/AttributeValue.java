package com.headstorm.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.*;
import java.util.UUID;

@NodeEntity
public class AttributeValue extends Entity {

    @Relationship(type="ASSIGNED_TO")
    ObjectInstance objectInstance;

    @Relationship
    Attribute attribute;

    @Property @JsonProperty
    String attributeName;

    @Property @JsonProperty
    Object value;

    private AttributeValue(String guid, ObjectInstance instance, Attribute attribute, Object value) {
        this.guid = guid;
        this.objectInstance = instance;
        this.attribute = attribute;
        this.value = value;
        this.attributeName = attribute.name;
    }

    public AttributeValue() {

    }

    public static AttributeValue createNew(ObjectInstance instance, Attribute attribute, Object value) {
        return new AttributeValue(UUID.randomUUID().toString(), instance, attribute, value);
    }

    @Override
    public String toString() {
        return "AttributeValue{" +
                "objectInstance=" + objectInstance.guid +
                ", attributeName='" + attributeName + '\'' +
                ", value=" + value +
                '}';
    }
}