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
package com.alibaba.json.test;

import java.io.InputStream;
import java.text.NumberFormat;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import junit.framework.TestCase;

public class Bug_0_Test extends TestCase {

    private String text;
    private int    COUNT = 1000;

    protected void setUp() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("json/Bug_0_Test.json");
        text = IOUtils.toString(is);
        is.close();

        // text =
        // "[{\"S\":321061,\"T\":\"GetAttributeResp\"},{\"ERROR\":null,\"TS\":0,\"VAL\":{\"SqlList\":[{\"BatchSizeMax\":0,\"BatchSizeTotal\":0,\"ConcurrentMax\":1,\"DataSource\":\"jdbc:wrap-jdbc:filters=default,encoding:name=ds-offer:jdbc:mysql://172.29.61.63:8066/amoeba\",\"EffectedRowCount\":0,\"ErrorCount\":0,\"ExecuteCount\":5,\"FetchRowCount\":5,\"File\":null,\"ID\":2001,\"LastError\":null,\"LastTime\":1292742908178,\"MaxTimespan\":16,\"MaxTimespanOccurTime\":1292742668191,\"Name\":null,\"RunningCount\":0,\"SQL\":\"SELECT @@SQL_MODE\",\"TotalTime\":83}]}}]";
    }

    public void test_0() throws Exception {
        for (int i = 0; i < 50; ++i) {
            // f_ali_json();
            f_jackson();
        }
    }

    private void f_ali_json() {
        long startNano = System.nanoTime();
        for (int i = 0; i < COUNT; ++i) {
            com.alibaba.fastjson.JSON.parse(text);
        }
        long nano = System.nanoTime() - startNano;
        System.out.println(NumberFormat.getInstance().format(nano));
    }

    private void f_jackson() throws Exception {
        long startNano = System.nanoTime();
        for (int i = 0; i < COUNT; ++i) {
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode node = (ArrayNode) mapper.readTree(text);
            JsonNode head = node.get(0);
            JsonNode body = node.get(1);
        }
        long nano = System.nanoTime() - startNano;
        System.out.println(NumberFormat.getInstance().format(nano));
    }

}
