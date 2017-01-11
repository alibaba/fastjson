package com.alibaba.json.test.a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.json.bvt.bug.Bug_101_for_rongganlin_case2;
import com.alibaba.json.test.vans.VansData;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by wenshao on 05/01/2017.
 */
public class VRTest_compare extends TestCase {
    public void test_vr() throws Exception {
        File file2 = new File("/Users/wenshao/Downloads/model_p_30687_r.json");
        String str = FileUtils.readFileToString(file2);

        VansData vansData = JSON.parseObject(str, VansData.class);
        String str2 = JSON.toJSONString(vansData);

        JSONObject a = JSON.parseObject(str);
        JSONObject b = JSON.parseObject(str2);

         assertEquals(str.length(), str2.length());
//         assertEquals(a.toJSONString().length(), b.toJSONString().length());

        diff (a, b);
    }

    void diff(JSONObject a, JSONObject b) {
        if (a.size() != b.size()) {
            System.out.println("map diff size");
            return;
        }

        for (Map.Entry entry : a.entrySet()) {
            String key = (String) entry.getKey();
            Object v = entry.getValue();
            Object v1 = b.get(key);

            if (v.getClass() != v1.getClass()) {
                System.out.println("map item diff class");
                return;
            }

            if (v instanceof JSONObject) {
                diff((JSONObject) v, (JSONObject) v1);
            } else if (v instanceof JSONArray) {
                diff((JSONArray) v, (JSONArray) v1);
            } else {
                diff(v, v1);
            }
        }
    }

    void diff(JSONArray a, JSONArray b) {
        if (a.size() != b.size()) {
            System.out.println("array diff size");
            return;
        }

        for (int i = 0; i < a.size(); ++i) {
            Object v = a.get(i);
            Object v1 = b.get(i);

            if (v.getClass() != v1.getClass()) {
                System.out.println("array item diff class");
            }

            if (v instanceof JSONObject) {
                diff((JSONObject) v, (JSONObject) v1);
            } else if (v instanceof JSONArray) {
                diff((JSONArray) v, (JSONArray) v1);
            } else {
                diff(v, v1);
            }
        }
    }

    void diff(Object a, Object b) {
        if (a instanceof BigDecimal) {
            String sa = JSON.toJSONString(a);
            String sb = JSON.toJSONString(b);
            if (!sa.equals(sb)) {
                System.out.println("diff decimal. " + sa + " -> " + sb);
            }
            return;
        }
        if (!a.equals(b)) {
            System.out.println("diff value. " + a.getClass());
        }
    }
}
