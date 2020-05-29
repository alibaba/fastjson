package com.alibaba.fastjson.serializer;

import java.util.HashSet;
import java.util.Set;

/*
 * Copyright 1999-2018 Alibaba Group.
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
public class SimplePropertyPreFilter implements PropertyPreFilter {

    private final Class<?>    clazz;
    private final Set<String> includes = new HashSet<String>();
    private final Set<String> excludes = new HashSet<String>();
    private int               maxLevel = 0;

    public SimplePropertyPreFilter(String... properties){
        this(null, properties);
    }

    public SimplePropertyPreFilter(Class<?> clazz, String... properties){
        super();
        this.clazz = clazz;
        for (String item : properties) {
            if (item != null) {
                this.includes.add(item);
            }
        }
    }
    
    /**
     * @since 1.2.9
     */
    public int getMaxLevel() {
        return maxLevel;
    }
    
    /**
     * @since 1.2.9
     */
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Set<String> getIncludes() {
        return includes;
    }

    public Set<String> getExcludes() {
        return excludes;
    }

    public boolean apply(JSONSerializer serializer, Object source, String name) {
        if (source == null) {
            return true;
        }

        if (clazz != null && !clazz.isInstance(source)) {
            return true;
        }

        if (this.excludes.contains(name)) {
            return false;
        }
        
        if (maxLevel > 0) {
            int level = 0;
            SerialContext context = serializer.context;
            while (context != null) {
                level++;
                if (level > maxLevel) {
                    return false;
                }
                context = context.parent;
            }
        }

        if (includes.size() == 0 || includes.contains(name)) {
            return true;
        }
        
        return false;
    }

}
