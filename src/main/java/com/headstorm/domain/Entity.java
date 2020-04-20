package com.headstorm.domain;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

abstract public class Entity {

    @Id
    public String guid;

    @Relationship(type="INCOMING_ENTITY")
    public Set<Entity> incomingEntityRelationships;

    @Relationship(type="OUTGOING_ENTITY")
    public Set<Entity> outgoingEntityRelationships;
}
