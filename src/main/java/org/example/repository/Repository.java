package org.example.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<E, ID> {
    Optional<E> save(E entity) ;
    List<E> findAll();
    Optional<E> getById(ID id);
    Optional<E> update(E entity);
    void delete(ID id);
}