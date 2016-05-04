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

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.TestCase;

public class JSONParser2Test extends TestCase {

    private String text;
    private int    COUNT = 1000 * 10;

    protected void setUp() throws Exception {
        // String resource = "json/Bug_0_Test.json";
        String resource = "json/Bug_0_Test.json";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        text = IOUtils.toString(is);
        is.close();

        // text =
        // "[{\"S\":321061,\"T\":\"GetAttributeResp\"},{\"ERROR\":null,\"TS\":0,\"VAL\":{\"SqlList\":[{\"BatchSizeMax\":0,\"BatchSizeTotal\":0,\"ConcurrentMax\":1,\"DataSource\":\"jdbc:wrap-jdbc:filters=default,encoding:name=ds-offer:jdbc:mysql://100.10.10.10:8066/xxx\",\"EffectedRowCount\":0,\"ErrorCount\":0,\"ExecuteCount\":5,\"FetchRowCount\":5,\"File\":null,\"ID\":2001,\"LastError\":null,\"LastTime\":1292742908178,\"MaxTimespan\":16,\"MaxTimespanOccurTime\":1292742668191,\"Name\":null,\"RunningCount\":0,\"SQL\":\"SELECT @@SQL_MODE\",\"TotalTime\":83}]}}]";
        // text = "{\"name\":null,\"flag\":true}";
        // text = "-6470204979932713723";
        // text = "[-5.041598256063065E-20,-7210028408342716000]";
        // text = "[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19]";
        // text = "{\"S\":321061,\"T\":\"GetAttributeResp\"}";
        // text = "{\"?�Z,??\": true,\"?>�wj��\\\"\\\\�_c~M?SN?!?k$|HE��K��'\": -1377757625945773954}";
    }

    public void test_0() throws Exception {
        for (int i = 0; i < 50; ++i) {
            f_ali_json();
            f_jackson();
            // f_simple_json();

            System.out.println();
        }

        System.out.println();
        System.out.println(text);
    }

    public void f_ali_json() throws Exception {
        // String input = "[{\"a\":3}]";
        long startNano = System.nanoTime();
        for (int i = 0; i < COUNT; ++i) {
            DefaultJSONParser parser = new DefaultJSONParser(text);
            parser.parse();
        }
        long nano = System.nanoTime() - startNano;
        System.out.println("fast-json \t: " + NumberFormat.getInstance().format(nano));
    }

    private void f_jackson() throws Exception {
        long startNano = System.nanoTime();
        for (int i = 0; i < COUNT; ++i) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(text);
            // JsonNode head = node.get(0);
            // JsonNode body = node.get(1);
        }
        long nano = System.nanoTime() - startNano;
        System.out.println("jackson \t: " + NumberFormat.getInstance().format(nano));
    }
}
