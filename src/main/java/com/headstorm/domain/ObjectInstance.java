package com.headstorm.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@NodeEntity
public class ObjectInstance extends Entity {

    public String name;

//    @Relationship(type = "INSTANCE_OF")
//    public Template template;

    @Property
    public String templateReference;

    @Property
    public String type;

    @Relationship(type = "HAS_VALUE")
    public List<AttributeValue> attributeValueList;

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
