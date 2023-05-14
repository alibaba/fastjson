/*
 * Copyright 2015-2016 Dark Phoenixs (Open-Source Organization).
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
package com.alibaba.json.bvt.support.spring.mock.entity;

/**
 * <p>MgQueryCondition</p>
 * <p></p>
 *
 * @author Victor.Zxy
 * @version 1.0
 * @since 2016年8月8日
 */
public class FastJsonGenericityTestVO<T> {

    private T t;

    /**
     * @return the t
     */
    public T getT() {
        return t;
    }

    /**
     * @param t the t to set
     */
    public void setT(T t) {
        this.t = t;
    }
}
