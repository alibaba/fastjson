package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;
import org.glassfish.jersey.server.JSONP;

public class JSONPath_7 extends TestCase {

    public void test_path() throws Exception {
        Model[] array = new Model[] {new Model(101), new Model(202), new Model(303)};
        JSONArray values = (JSONArray) JSONPath.eval(array, "$.id");
        assertEquals(101, values.get(0));
        assertEquals(202, values.get(1));
        assertEquals(303, values.get(2));

        assertEquals(3, JSONPath.eval(array, "$.length"));
    }

    public static class Model {
        public int id;

        public Model(int id) {
            this.id = id;
        }
    }

//    public void test_path_2() throws Exception {
////        File file = new File("/Users/wenshao/Downloads/test");
////        String json = FileUtils.readFileToString(file);
//        String json = "{\"returnObj\":[{\"$ref\":\"$.subInvokes.com\\\\.alipay\\\\.cif\\\\.user\\\\.UserInfoQueryService\\\\@findUserInfosByCardNo\\\\(String[])[0].response[0]\"}]}";
//        JSON.parseObject(json);
//    }

}
