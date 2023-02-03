package com.es.phoneshop.dao;

import com.es.phoneshop.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class GenericDao<T extends Item> implements Dao<T>{
    private List<T> objects = new ArrayList<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();
    private AtomicLong maxId = new AtomicLong();

    public T get(Long id) {
        readLock.lock();
        try {
            return objects.stream()
                    .filter(obj -> id.equals(obj.getId()))
                    .findAny()
                    .orElse(null);
        } finally {
            readLock.unlock();
        }

    }

    public void save(T object) {
        writeLock.lock();
        Long id = object.getId();
        if (id != null) {
            for (int i = 0; i < objects.size(); i++) {
                if (id.equals(objects.get(i).getId())) {
                    objects.set(i, object);
                    break;
                }
            }
        } else {
            object.setId(maxId.getAndIncrement());
            objects.add(object);
        }
        writeLock.unlock();
    }

    public List<T> getObjects() {
        readLock.lock();
        try {
            return objects;
        } finally {
            readLock.unlock();
        }
    }
}
