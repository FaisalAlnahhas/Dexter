package com.headstorm.domain;

public interface ObjectRegistry<T> {

    public Iterable<T> findAll();

    public T findByGuid(String guid);

}
