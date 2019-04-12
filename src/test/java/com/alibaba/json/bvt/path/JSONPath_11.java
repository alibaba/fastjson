package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

public class JSONPath_11 extends TestCase {

    public void test(){
        String json = "[\n" +
                "  [\n" +
                "    {\n" +
                "      \"CN\": \"t1c1CN\",\n" +
                "      \"FP\": \"t1c1FP\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"CN\": \"t1c2CN\",\n" +
                "      \"FP\": \"t1c2FP\"\n" +
                "    }\n" +
                "  ],\n" +
                "  [\n" +
                "    {\n" +
                "      \"CN\": \"t2c1CN\",\n" +
                "      \"FP\": \"t2c1FP\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"CN\": \"t2c2CN\",\n" +
                "      \"FP\": \"t2c2FP\"\n" +
                "    }\n" +
                "  ]\n" +
                "]";

        JSONArray array = JSON.parseArray(json);
        System.out.println(JSONPath.eval(array, "$[0,1][CN='t1c1CN']"));
    }

//    public void test_path_2() throws Exception {
////        File file = new File("/Users/wenshao/Downloads/test");
////        String json = FileUtils.readFileToString(file);
//        String json = "{\"returnObj\":[{\"$ref\":\"$.subInvokes.com\\\\.alipay\\\\.cif\\\\.user\\\\.UserInfoQueryService\\\\@findUserInfosByCardNo\\\\(String[])[0].response[0]\"}]}";
//        JSON.parseObject(json);
//    }

}
