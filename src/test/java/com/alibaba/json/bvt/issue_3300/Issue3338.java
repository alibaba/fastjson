package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ：Nanqi
 * @Date ：Created in 17:11 2020/7/11
 */
public class Issue3338 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        Map map = new HashMap();
        map.put("nanqi", "因为相信，所以看见。");
        model.setMap(map);

        String jsonString = JSONObject.toJSONString(model);
        assertTrue(jsonString.contains("因为相信，所以看见。"));

        Model modelBack = JSONObject.parseObject(jsonString, Model.class);
        assertEquals("因为相信，所以看见。", modelBack.getMap().get("nanqi"));
    }

    static class Model {
        private Map map;

        public Map getMap() {
            return map;
        }

        public void setMap(Map map) {
            this.map = map;
        }
    }
}
