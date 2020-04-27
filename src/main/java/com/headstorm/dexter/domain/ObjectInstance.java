package com.headstorm.dexter.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;
import java.util.stream.Collectors;

@NodeEntity
public class ObjectInstance extends Entity {

    public String name;

    @Property
    public String templateReference;

    @Property
    public String type;

    @Relationship(type = "HAS_VALUE")
    public List<AttributeValue> attributeValueList = new ArrayList<>();

    public List<AttributeValue> valuesForAttribute(String attributeName) {
        return attributeValueList.stream().filter(x -> x.attributeName.equals(attributeName)).collect(Collectors.toList());
    }

    transient public Map<String, Object> attributeValueMap;

    public static ObjectInstance createNew(String name, Template template, Map<String, Object> attributeValues) {
        return new ObjectInstance(UUID.randomUUID().toString(), name, template, attributeValues);
    }

    private ObjectInstance(String guid, String name, Template template, Map<String, Object> attributeValueMap) {
        this.guid = guid;
        this.name = name;
        this.type = template.name;
        this.templateReference = template.guid;
        if (attributeValueMap == null) {
            this.attributeValueMap = new HashMap<>();
        } else {
            this.attributeValueMap = attributeValueMap;
        }
    }

    public ObjectInstance() {

    }

    @Override
    public String toString() {
        return "ObjectInstance{" +
                "name='" + name + '\'' +
                ", guid='" + guid + '\'' +
                ", templateReference='" + templateReference + '\'' +
                ", type='" + type + '\'' +
                ", attributeValueList=" + attributeValueList +
                ", attributeValueMap=" + attributeValueMap +
                '}';
    }
}
