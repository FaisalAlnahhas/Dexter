package com.headstorm.domain;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity
public class EntityRelationship {

    @Id
    @GeneratedValue
    public Long id;

    @StartNode
    public Entity source;

    @EndNode
    public Entity target;
}
