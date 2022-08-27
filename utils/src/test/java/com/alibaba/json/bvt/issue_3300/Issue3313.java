package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import lombok.Data;
import org.springframework.util.Assert;

/**
 * @Author ：Nanqi
 * @Date ：Created in 21:54 2020/6/30
 */
public class Issue3313 extends TestCase {
    public void test_for_issue() throws Exception {
        String jsonStr = "{\"NAME\":\"nanqi\",\"age\":18}";
        Model model = JSONObject.parseObject(jsonStr, Model.class);
        Assert.notNull(model.getAGe());
        Assert.notNull(model.getName());
    }

    @Data
    static class Model {
        @JSONField(name = "NaMe")
        private String name;

        @JSONField(name = "age")
        private Integer aGe;
    }
}
