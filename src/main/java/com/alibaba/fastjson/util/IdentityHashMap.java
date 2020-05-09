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

import java.util.Arrays;

/**
 * for concurrent IdentityHashMap
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
@SuppressWarnings("unchecked")
public class IdentityHashMap<K, V> {
    private final Entry<K, V>[] buckets;
    private final int           indexMask;
    public final static int DEFAULT_SIZE = 8192;

    public IdentityHashMap(){
        this(DEFAULT_SIZE);
    }

    public IdentityHashMap(int tableSize){
        this.indexMask = tableSize - 1;
        this.buckets = new Entry[tableSize];
    }

    public final V get(K key) {
        final int hash = System.identityHashCode(key);
        final int bucket = hash & indexMask;

        for (Entry<K, V> entry = buckets[bucket]; entry != null; entry = entry.next) {
            if (key == entry.key) {
                return (V) entry.value;
            }
        }

        return null;
    }

    public Class findClass(String keyString) {
        for (int i = 0; i < buckets.length; i++) {
            Entry bucket = buckets[i];

            if (bucket == null) {
                continue;
            }

            for (Entry<K, V> entry = bucket; entry != null; entry = entry.next) {
                Object key = bucket.key;
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
        final int hash = System.identityHashCode(key);
        final int bucket = hash & indexMask;

        for (Entry<K, V> entry = buckets[bucket]; entry != null; entry = entry.next) {
            if (key == entry.key) {
                entry.value = value;
                return true;
            }
        }

        Entry<K, V> entry = new Entry<K, V>(key, value, hash, buckets[bucket]);
        buckets[bucket] = entry;  // 并发是处理时会可能导致缓存丢失，但不影响正确性

        return false;
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

    public void clear() {
        Arrays.fill(this.buckets, null);
    }
}
