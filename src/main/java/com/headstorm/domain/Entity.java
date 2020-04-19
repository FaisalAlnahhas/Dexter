package com.headstorm.domain;

import org.neo4j.ogm.annotation.Id;

abstract public class Entity {

    @Id
    public String guid;
}
