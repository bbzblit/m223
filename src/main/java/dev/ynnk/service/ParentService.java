package dev.ynnk.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
interface ParentService<T, ID> {
    T save(T entity);

    T findById(ID id);

    Iterable<T> findAll();

    void deleteById(ID id);
}
