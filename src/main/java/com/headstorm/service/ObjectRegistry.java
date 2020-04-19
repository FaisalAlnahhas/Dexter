package com.headstorm.service;

import com.headstorm.domain.Entity;
import com.headstorm.neo4j.Neo4jSessionFactory;
import org.neo4j.ogm.session.Session;

public class ObjectRegistry<T extends Entity> implements Service<T> {

    private static final int DEPTH_LIST = 1;
    private static final int DEPTH_ENTITY = 1;
    private final Class<T> clazz;
    protected Session session = Neo4jSessionFactory.getInstance().getNeo4jSession();

    public ObjectRegistry(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Iterable<T> findAll() {
        return session.loadAll(clazz, DEPTH_LIST);
    }

    @Override
    public T find(String guid) {
        return session.load(clazz, guid, DEPTH_ENTITY);
    }

    @Override
    public void delete(String guid) {
        session.delete(session.load(clazz, guid));
    }

    @Override
    public T createOrUpdate(T entity) {
        session.save(entity, DEPTH_ENTITY);
        return find(entity.guid);
    }

    public void deleteAll() {
        for (T x : findAll()) {
            delete(x.guid);
        }
    }
}
