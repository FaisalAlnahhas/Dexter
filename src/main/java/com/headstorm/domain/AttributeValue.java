package com.headstorm.domain;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity
public class AttributeValue {

    @Id @GeneratedValue
    Long id;

    @StartNode
    ObjectInstance objectInstance;

    @EndNode
    Attribute attribute;

    Object value;

    public AttributeValue(ObjectInstance instance, Attribute attribute, Object value) {
        this.objectInstance = instance;
        this.attribute = attribute;
        this.value = value;
    }
}