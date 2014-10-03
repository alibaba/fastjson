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
package com.alibaba.fastjson.serializer;

/**
 * circular references detect
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
public final class JSONSerializerContext {

    public static final int DEFAULT_TABLE_SIZE = 128;

    private final Entry[]   buckets;
    private final int       indexMask;

    public JSONSerializerContext(){
        this(DEFAULT_TABLE_SIZE);
    }

    public JSONSerializerContext(int tableSize){
        this.indexMask = tableSize - 1;
        this.buckets = new Entry[tableSize];
    }

    public final boolean put(Object o) {
        final int hash = System.identityHashCode(o);
        final int bucket = hash & indexMask;

        for (Entry entry = buckets[bucket]; entry != null; entry = entry.next) {
            if (o == entry.object) {
                return true;
            }
        }

        Entry entry = new Entry(o, hash, buckets[bucket]);
        buckets[bucket] = entry;

        return false;
    }

    protected static final class Entry {

        public final int    hashCode;
        public final Object object;

        public Entry        next;

        public Entry(Object object, int hash, Entry next){
            this.object = object;
            this.next = next;
            this.hashCode = hash;
        }

    }
}
