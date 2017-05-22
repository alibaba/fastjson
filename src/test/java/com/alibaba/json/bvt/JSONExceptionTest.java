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
package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;

public class JSONExceptionTest extends TestCase {

    public void test_all() throws Exception {
        Assert.assertEquals("xxx", new JSONException("xxx").getMessage());
        Assert.assertEquals(null, new JSONException().getMessage());
        Assert.assertEquals("xxx", new JSONException("xxx", new RuntimeException()).getMessage());
    }
}
