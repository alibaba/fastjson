package com.alibaba.json.bvt.parser.creator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

public class JSONCreatorTest10 extends TestCase {
    public void test_for_yk() throws Exception {
        String jsonString = "{\"link\":\"http://lqgzs.org/fsqhwlnf\",\"text\":\"乐动力专享\"}";
        JSONObject headerJSON = JSONObject.parseObject(jsonString);
        HeaderDTO headerDTO = headerJSON.toJavaObject(HeaderDTO.class);

        assertEquals("http://lqgzs.org/fsqhwlnf", headerDTO.link);
        assertEquals("乐动力专享", headerDTO.title);
    }

    public static class HeaderDTO {
        private String title;
        private String link;

        @JSONCreator
        public HeaderDTO(@JSONField(name = "text") String title,@JSONField(name = "link") String link) {
            this.title = title;
            this.link = link;
        }
    }
}
