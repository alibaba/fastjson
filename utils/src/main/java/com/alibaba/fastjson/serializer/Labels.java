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
package com.alibaba.fastjson.serializer;

import java.util.Arrays;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class Labels {

    private static class DefaultLabelFilter implements LabelFilter {

        private String[] includes;
        private String[] excludes;

        public DefaultLabelFilter(String[] includes, String[] excludes){
            if (includes != null) {
                this.includes = new String[includes.length];
                System.arraycopy(includes, 0, this.includes, 0, includes.length);
                Arrays.sort(this.includes);
            }
            if (excludes != null) {
                this.excludes = new String[excludes.length];
                System.arraycopy(excludes, 0, this.excludes, 0, excludes.length);
                Arrays.sort(this.excludes);
            }
        }

        public boolean apply(String label) {
            if (excludes != null) {
                return Arrays.binarySearch(excludes, label) < 0;
            }

            return includes != null // 
                    && Arrays.binarySearch(includes, label) >= 0;
        }
    }

    public static LabelFilter includes(String... views) {
        return new DefaultLabelFilter(views, null);
    }

    public static LabelFilter excludes(String... views) {
        return new DefaultLabelFilter(null, views);
    }
}
