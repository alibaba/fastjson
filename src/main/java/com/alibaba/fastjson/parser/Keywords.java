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
package com.alibaba.fastjson.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public class Keywords {

    private final Map<String, Integer> keywords;

    public static Keywords               DEFAULT_KEYWORDS;

    static {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("null", JSONToken.NULL);
        map.put("new", JSONToken.NEW);
        map.put("true", JSONToken.TRUE);
        map.put("false", JSONToken.FALSE);
        DEFAULT_KEYWORDS = new Keywords(map);
    }

    public Keywords(Map<String, Integer> keywords){
        this.keywords = keywords;
    }

    public Integer getKeyword(String key) {
        return keywords.get(key);
    }

}
