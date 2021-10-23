package com.alibaba.json.bvt.issue_3900;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

/**
 * @description: 1.2.70以上版本toJavaObject无法序列化JSONField别名中有"_"的字段
 * @author: lizz
 * @date: 2021/10/21 16:30
 * <p>
 * https://github.com/alibaba/fastjson/issues/3248
 */
public class Issue3930 {
    @Test
    public void JSONObjectTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ab", "11");
        jsonObject.put("cd", "22");
        jsonObject.put("ef", "33");
        Lizz lizz = jsonObject.toJavaObject(Lizz.class);
        System.out.println(JSON.toJSONString(lizz));
        Assert.assertNotNull(lizz.getAb());
        Assert.assertNotNull(lizz.getCd());
        Assert.assertNotNull(lizz.getE_f());
    }

    @Data
    static class Lizz {
        @JSONField(name = "a_b")
        private String ab;
        private String cd;
        private String e_f;
    }
}
