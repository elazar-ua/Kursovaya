package com.hpcc.kursovaya.dao.my_type.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.Ignore;

public class RealmHashMap<K, V> implements RealmModel, Map<K, V>, Cloneable, Serializable {
    private static final long serialVersionUID = 362498820763181265L;

    @Ignore
    private HashMap<K,V> hashMap;

    private RealmList<K> key;
    private RealmList<V> value;

    {
        key = new RealmList<>();
        value = new RealmList<>();
    }
    public RealmHashMap() {
        hashMap = new HashMap<>();
    }
    public RealmHashMap(int initialCapacity) {
        hashMap = new HashMap<>(initialCapacity);
    }
    public RealmHashMap(int initialCapacity, float loadFactor) {
        hashMap = new HashMap<>(initialCapacity, loadFactor);
    }
    public RealmHashMap(Map<? extends K, ? extends V> m) {
        hashMap = new HashMap<>(m);
    }

    @Override
    public int size() {
        return hashMap.size();
    }

    @Override
    public boolean isEmpty() {
        return hashMap.isEmpty();
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        return hashMap.containsKey(key);
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        return hashMap.containsValue(value);
    }

    @Nullable
    @Override
    public V get(@Nullable Object key) {
        return hashMap.get(key);
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        if (!containsKey(key)){
            this.key.add(key);
            this.value.add(value);
        } else {
            int index = this.key.indexOf(key);
            this.value.set(index, value);
        }
        return hashMap.put(key, value);
    }

    @Nullable
    @Override
    public V remove(@Nullable Object key) {
        int index = this.key.indexOf(key);
        this.value.remove(index);
        return hashMap.remove(key);
    }

    @Override
    public void putAll(@NonNull Map<? extends K, ? extends V> m) {
        hashMap.putAll(m);
    }

    @Override
    public void clear() {
        hashMap.clear();
    }

    @NonNull
    @Override
    public Set<K> keySet() {
        return hashMap.keySet();
    }

    @NonNull
    @Override
    public Collection<V> values() {
        return hashMap.values();
    }

    @NonNull
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return hashMap.entrySet();
    }

    @Override
    public int hashCode() {
        return hashMap.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return hashMap.equals(obj);
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return hashMap.clone();
    }

    @NonNull
    @Override
    public String toString() {
        return hashMap.toString();
    }
}
