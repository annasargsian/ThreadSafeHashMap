package com.company;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;


public class ThreadSafeHashMap<K, V> implements Map<K, V> {

    private final Map<K, V> map = new HashMap<K, V>();
    private ReadWriteLock locked = new ReentrantReadWriteLock();


    @Override
    public int size() {
        return doWithReadLock(map::size);
    }

    @Override
    public boolean isEmpty() {
        return doWithReadLock(map::isEmpty);
    }

    @Override
    public boolean containsKey(Object key) {
        return doWithReadLock(() -> map.containsKey(key));
    }

    @Override
    public boolean containsValue(Object value) {
        return doWithReadLock(() -> map.containsValue(value));
    }

    @Override
    public V get(Object key) {
        return doWithReadLock(() -> map.get(key));
    }

    @Override
    public V put(K key, V value) {
        return doWithWriteLock(() -> {
            V put = map.put(key, value);
            System.out.println(Thread.currentThread().getName() + " " + map);
            return put;
        });

    }

    @Override
    public V remove(Object key) {
        return doWithWriteLock(() -> map.remove(key));
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        locked.writeLock().lock();
        try {
            map.putAll(m);
        } finally {
            locked.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        locked.writeLock().lock();
        try {
            map.clear();
        } finally {
            locked.writeLock().unlock();
        }
    }

    @Override
    public Set<K> keySet() {
        return doWithWriteLock(map::keySet);
    }

    @Override
    public Collection<V> values() {
        return doWithReadLock(map::values);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return doWithReadLock(map::entrySet);
    }


    private <R> R doWithReadLock(Supplier<R> supplier) {
        this.locked.readLock().lock();
        try {
            return supplier.get();
        } finally {
            this.locked.readLock().unlock();
        }
    }

    private <R> R doWithWriteLock(Supplier<R> supplier) {
        this.locked.writeLock().lock();
        try {
            return supplier.get();
        } finally {
            this.locked.writeLock().unlock();
        }
    }

    @Override
    public String toString() {
        locked.writeLock().lock();
        try {
            return "ThreadSafeHashMap{" +
                    "map=" + map +
                    '}';
        } finally {
            locked.writeLock().unlock();
        }
    }
}
