package com.es.phoneshop.dao;

import com.es.phoneshop.model.Item;

public interface Dao<T extends Item> {
     T get(Long id);
     void save(T object);
}
