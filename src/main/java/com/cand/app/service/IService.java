package com.cand.app.service;

import java.util.Set;

public interface IService<T> {

    void save(T t);

    void saveAll(Set<T> ts);

    Set<T> getAll();

}
