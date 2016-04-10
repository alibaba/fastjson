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

import java.lang.reflect.Type;

/**
 * for concurrent IdentityHashMap
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
@SuppressWarnings("unchecked")
public class IdentityHashMap<V> {
    private final Entry<V>[] buckets;
    private final int        indexMask;

    public IdentityHashMap(int tableSize){
        this.indexMask = tableSize - 1;
        this.buckets = new Entry[tableSize];
    }

    public final V get(Type key) {
        final int hash = System.identityHashCode(key);
        final int bucket = hash & indexMask;

        for (Entry<V> entry = buckets[bucket]; entry != null; entry = entry.next) {
            if (key == entry.key) {
                return (V) entry.value;
            }
        }

        return null;
    }

    public boolean put(Type key, V value) {
        final int hash = System.identityHashCode(key);
        final int bucket = hash & indexMask;

        for (Entry<V> entry = buckets[bucket]; entry != null; entry = entry.next) {
            if (key == entry.key) {
                entry.value = value;
                return true;
            }
        }

        Entry<V> entry = new Entry<V>(key, value, hash, buckets[bucket]);
        buckets[bucket] = entry;  // 并发是处理时会可能导致缓存丢失，但不影响正确性

        return false;
    }

    protected static final class Entry<V> {
        public final int      hashCode;
        public final Type     key;
        public V              value;

        public final Entry<V> next;

        public Entry(Type key, V value, int hash, Entry<V> next){
            this.key = key;
            this.value = value;
            this.next = next;
            this.hashCode = hash;
        }
    }

}
