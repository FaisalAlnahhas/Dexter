package com.headstorm.domain;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;


abstract public class Entity {

    @Id
    public String guid;

    @Property
    public String entityType = getEntityType();

    transient private Class<?> concreteClass = this.getClass();

    public Object toConcreteType() {
        return concreteClass.cast(this);
    }

    public String getEntityType() {
        return this.getClass().getSimpleName();
    }
}
