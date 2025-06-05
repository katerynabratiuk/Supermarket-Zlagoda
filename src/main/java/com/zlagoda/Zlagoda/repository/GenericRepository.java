package com.zlagoda.Zlagoda.repository;

import java.util.List;
/*
    E - entity
    K - key
 */

public interface GenericRepository<E,K> {

    List<E> findAll();
    List<E> findByName(String name);
    E findById(K k);
    void create(E k);
    void update(E k);
    void delete(K k);

}
