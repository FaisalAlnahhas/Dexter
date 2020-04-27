package com.headstorm.dexter.client;

interface Service<T> {

    Iterable<T> findAll();

    T find(String guid);

    void delete(String guid);

    T createOrUpdate(T object);

}
