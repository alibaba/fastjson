/*
 * Copyright 1999-2101 Alibaba Group.
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

import java.util.concurrent.atomic.AtomicReference;

/**
 * for concurrent IdentityHashMap
 * 
 * @author wenshao<szujobs@hotmail.com>
 */
@SuppressWarnings("unchecked")
public class IdentityHashMap<K, V> {

    public static final int     DEFAULT_TABLE_SIZE = 1024;

    private final AtomicReference<Entry<K, V>>[] buckets;
    private final int           indexMask;

    public IdentityHashMap(){
        this(DEFAULT_TABLE_SIZE);
    }

    public IdentityHashMap(int tableSize){
        this.indexMask = tableSize - 1;
        this.buckets = new AtomicReference[tableSize];
        for (int i = 0; i < buckets.length; i++) {
             buckets[i] = new AtomicReference<Entry<K, V>>();
        }
    }

    public final V get(K key) {
        final int hash = System.identityHashCode(key);
        final int bucket = hash & indexMask;

        for (Entry<K, V> entry = buckets[bucket].get(); entry != null; entry = entry.next) {
            if (key == entry.key) {
                return (V) entry.value;
            }
        }

        return null;
    }

    public boolean put(K key, V value) {
        final int hash = System.identityHashCode(key);
        final int bucket = hash & indexMask;

        for (;;) {
            Entry<K, V> oldEntry = buckets[bucket].get();
            for (Entry<K, V> entry = oldEntry; entry != null; entry = entry.next) {
                if (key == entry.key) {
                    entry.value = value;
                    return true;
                }
            }

            Entry<K, V> entry = new Entry<K, V>(key, value, hash, oldEntry);
            boolean b = buckets[bucket].compareAndSet(oldEntry, entry);
            if (b) {
                return false;
            }
        }
    }

    public int size() {
        int size = 0;
        for (int i = 0; i < buckets.length; ++i) {
            for (Entry<K, V> entry = buckets[i].get(); entry != null; entry = entry.next) {
                size++;
            }
        }
        return size;
    }

    protected static final class Entry<K, V> {

        public final int   hashCode;
        public final K     key;
        public V     value;

        public final Entry<K, V> next;

        public Entry(K key, V value, int hash, Entry<K, V> next){
            this.key = key;
            this.value = value;
            this.next = next;
            this.hashCode = hash;
        }
    }

}
