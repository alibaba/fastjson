/*
 * Copyright 1999-2017 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * for concurrent IdentityHashMap
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
@SuppressWarnings("unchecked")
public class IdentityWeakHashMap<K, V> {
    private final Entry<K, V>[] buckets;
    private final int           indexMask;
    public final static int DEFAULT_SIZE = 1024;
    private final List<V> keepRefs = new ArrayList<V>();

    public IdentityWeakHashMap(){
        this(DEFAULT_SIZE);
    }

    public IdentityWeakHashMap(int tableSize){
        this.indexMask = tableSize - 1;
        this.buckets = new Entry[tableSize];
    }

    public final V get(K key) {
        final int hash = System.identityHashCode(key);
        final int bucket = hash & indexMask;

        for (Entry<K, V> entry = buckets[bucket]; entry != null; entry = entry.next) {
            K entryKey = entry.key.get();
            V entryValue = entry.getValue();

            if ((entryKey == null) || (entryValue == null)) {
                if (entry.prev == null) {
                    buckets[bucket] = entry.next;
                } else {
                    entry.prev.next = entry.next;
                }

                continue;
            }

            if (key == entryKey) {
                return entryValue;
            }
        }

        return null;
    }

    public Class findClass(String keyString) {
        for (Entry bucket : buckets) {
            if (bucket == null) {
                continue;
            }

            for (Entry<K, V> entry = bucket; entry != null; entry = entry.next) {
                Object key = bucket.key.get();

                if ((key == null) || (bucket.getValue() == null)) {
                    continue;
                }

                if (key instanceof Class) {
                    Class clazz = ((Class) key);
                    String className = clazz.getName();
                    if (className.equals(keyString)) {
                        return clazz;
                    }
                }
            }
        }

        return null;
    }

    public boolean put(K key, V value) {
        return put(key, value, true);
    }

    public boolean put(K key, V value, boolean keepRef) {
        if (keepRef) {
            keepRefs.add(value);
        }

        final int hash = System.identityHashCode(key);
        final int bucket = hash & indexMask;

        for (Entry<K, V> entry = buckets[bucket]; entry != null; entry = entry.next) {
            Object entryKey = entry.key.get();

            if ((entryKey == null) || (entry.getValue() == null)) {
                if (entry.prev == null) {
                    buckets[bucket] = entry.next;
                } else {
                    entry.prev.next = entry.next;
                }

                continue;
            }

            if (key == entryKey) {
                entry.setValue(value);
                return true;
            }
        }

        Entry<K, V> entry = new Entry<K, V>(key, value, hash, null, buckets[bucket]);

        if (entry.next != null) {
            entry.next.prev = entry;
        }

        buckets[bucket] = entry;  // 并发是处理时会可能导致缓存丢失，但不影响正确性

        return false;
    }

    protected static final class Entry<K, V> {

        public final int                  hashCode;
        public final WeakReference<K>     key;
        private WeakReference<V>          value;

        public Entry<K, V> prev;
        public Entry<K, V> next;

        public Entry(K key, V value, int hash, Entry<K, V> prev, Entry<K, V> next){
            this.key = new WeakReference<K>(key);
            this.value = new WeakReference<V>(value);
            this.prev = prev;
            this.next = next;
            this.hashCode = hash;
        }

        public V getValue() {
            return this.value.get();
        }

        public void setValue(V value) {
            this.value = new WeakReference<V>(value);
        }
    }

}
