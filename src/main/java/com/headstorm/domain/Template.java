package com.headstorm.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NodeEntity
public class Template extends Entity {

    @Property
    public String name;

    @Relationship(direction = Relationship.INCOMING)
    public Set<Attribute> requiredAttributes = new HashSet<>();

    @Relationship(direction = Relationship.INCOMING)
    public Set<Attribute> optionalAttributes = new HashSet<>();

    public Set<Attribute> getAllAttributes() {
        HashSet<Attribute> attributes = new HashSet<>(requiredAttributes);
        attributes.addAll(optionalAttributes);
        return attributes;
    }

    public static Template createNew(String name) {
        return new Template(UUID.randomUUID().toString(), name);
    }

    private Template(String guid, String name) {
        this.guid = guid;
        this.name = name;
    }

    public Template() {

    }

    public void addRequiredAttribute(Attribute attribute) {
        requiredAttributes.add(attribute);
    }
    public void addRequiredAttributes(Iterable<Attribute> attributes) {
        for (Attribute att : attributes)
        requiredAttributes.add(att);
    }

    public void addOptionalAttribute(Attribute attribute) {
        optionalAttributes.add(attribute);
    }
    public void addOptionalAttributes(Iterable<Attribute> attributes) {
        for (Attribute att : attributes)
            optionalAttributes.add(att);
    }

    @Override
    public String toString() {
        return "Template{" +
                "name='" + name + '\'' +
                ", requiredAttributes=" + requiredAttributes +
                ", optionalAttributes=" + optionalAttributes +
                '}';
    }
}
