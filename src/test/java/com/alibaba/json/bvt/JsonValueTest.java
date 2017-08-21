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

import java.util.Date;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class JsonValueTest extends TestCase {

    public void test_toJSONString() throws Exception {
        Assert.assertEquals("null", JSON.toJSONString(Double.NaN));
        Assert.assertEquals("3.0", JSON.toJSONString(3D));
        Assert.assertEquals("null", JSON.toJSONString(Float.NaN));
        Assert.assertEquals("3.0", JSON.toJSONString(3F));
        Assert.assertEquals("1292939095640", JSON.toJSONString(new Date(1292939095640L)));
        Assert.assertEquals(new Date(1292939095640L), JSON.parse("new Date(1292939095640)"));
    }

    public void test_bug_0() throws Exception {
        String text = "[{\"S\":0,\"T\":\"Register\"},{\"HOST_NAME\":\"qa-qd-62-187\",\"IP\":[\"172.29.62.187\"],\"MAC_ADDR\":[\"00:16:3E:43:E5:1C\"],\"SERVICE_TAG\":\"NOSN00:16:3E:43:E5:1C\",\"VERSION\":\"2.5\"}]  ";
        JSON.parseArray(text);
    }

    public void test_bug_1() throws Exception {
        String text = "[{\"S\":2,\"T\":\"ConnectResp\"},\n\r \t{\"VAL\" :null}]\r\f";
        JSON.parseArray(text);
    }

}
